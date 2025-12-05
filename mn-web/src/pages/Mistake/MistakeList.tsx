import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
    Card, Table, Button, Space, Tag, Select, Input, message, Popconfirm,
    Dropdown
} from 'antd'
import {
    PlusOutlined, SearchOutlined, DeleteOutlined, EditOutlined,
    RobotOutlined, MoreOutlined
} from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'
import { mistakeApi, type Mistake } from '../../api/mistake'

const subjectColors: Record<number, string> = {
    1: 'red',      // 语文
    2: 'blue',     // 数学
    3: 'green',    // 英语
    4: 'purple',   // 物理
    5: 'orange',   // 化学
    6: 'cyan',     // 生物
}

const subjectNames: Record<number, string> = {
    1: '语文',
    2: '数学',
    3: '英语',
    4: '物理',
    5: '化学',
    6: '生物',
    7: '历史',
    8: '地理',
    9: '政治',
}

const masteryStatusMap: Record<string, { text: string; color: string }> = {
    not_mastered: { text: '未掌握', color: 'red' },
    reviewing: { text: '复习中', color: 'orange' },
    mastered: { text: '已掌握', color: 'green' },
}

export default function MistakeList() {
    const navigate = useNavigate()
    const [loading, setLoading] = useState(false)
    const [data, setData] = useState<Mistake[]>([])
    const [total, setTotal] = useState(0)
    const [page, setPage] = useState(0)
    const [subjectId, setSubjectId] = useState<number | undefined>()
    const [analyzing, setAnalyzing] = useState<number | null>(null)

    const fetchData = async () => {
        setLoading(true)
        try {
            // TODO: 从登录用户获取 userId
            const userId = 1
            const res = await mistakeApi.list(userId, page, 10, subjectId)
            setData(res.content)
            setTotal(res.totalElements)
        } catch (error) {
            message.error('加载失败')
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchData()
    }, [page, subjectId])

    const handleDelete = async (id: number) => {
        try {
            await mistakeApi.delete(id)
            message.success('删除成功')
            fetchData()
        } catch (error) {
            message.error('删除失败')
        }
    }

    const handleAnalyze = async (id: number) => {
        setAnalyzing(id)
        try {
            const result = await mistakeApi.analyze(id)
            message.info({
                content: result.analysis.substring(0, 200) + '...',
                duration: 10
            })
        } catch (error) {
            message.error('分析失败')
        } finally {
            setAnalyzing(null)
        }
    }

    const columns: ColumnsType<Mistake> = [
        {
            title: '科目',
            dataIndex: 'subjectId',
            width: 80,
            render: (id: number) => (
                <Tag color={subjectColors[id] || 'default'}>
                    {subjectNames[id] || '未知'}
                </Tag>
            )
        },
        {
            title: '题目内容',
            dataIndex: 'questionContent',
            ellipsis: true,
            render: (text: string) => (
                <div style={{ maxWidth: 400 }}>
                    {text.length > 100 ? text.substring(0, 100) + '...' : text}
                </div>
            )
        },
        {
            title: '错误原因',
            dataIndex: 'errorReason',
            width: 100,
            render: (reason: string) => reason || '-'
        },
        {
            title: '掌握状态',
            dataIndex: 'masteryStatus',
            width: 100,
            render: (status: string) => {
                const config = masteryStatusMap[status] || { text: status, color: 'default' }
                return <Tag color={config.color}>{config.text}</Tag>
            }
        },
        {
            title: '创建时间',
            dataIndex: 'createdAt',
            width: 120,
            render: (date: string) => date ? new Date(date).toLocaleDateString() : '-'
        },
        {
            title: '操作',
            width: 150,
            render: (_, record) => (
                <Space>
                    <Button
                        type="link"
                        size="small"
                        icon={<EditOutlined />}
                        onClick={() => navigate(`/mistakes/${record.id}`)}
                    >
                        编辑
                    </Button>
                    <Button
                        type="link"
                        size="small"
                        icon={<RobotOutlined />}
                        loading={analyzing === record.id}
                        onClick={() => handleAnalyze(record.id!)}
                    >
                        AI分析
                    </Button>
                    <Popconfirm
                        title="确定删除这道错题吗？"
                        onConfirm={() => handleDelete(record.id!)}
                    >
                        <Button type="link" size="small" danger icon={<DeleteOutlined />}>
                            删除
                        </Button>
                    </Popconfirm>
                </Space>
            )
        }
    ]

    return (
        <div>
            <div className="page-header">
                <h2>我的错题本</h2>
                <Button
                    type="primary"
                    icon={<PlusOutlined />}
                    onClick={() => navigate('/mistakes/new')}
                >
                    添加错题
                </Button>
            </div>

            <Card>
                <div style={{ marginBottom: 16, display: 'flex', gap: 16 }}>
                    <Select
                        placeholder="选择科目"
                        allowClear
                        style={{ width: 150 }}
                        value={subjectId}
                        onChange={setSubjectId}
                        options={Object.entries(subjectNames).map(([id, name]) => ({
                            value: Number(id),
                            label: name
                        }))}
                    />
                    <Input
                        placeholder="搜索题目内容"
                        prefix={<SearchOutlined />}
                        style={{ width: 300 }}
                    />
                </div>

                <Table
                    columns={columns}
                    dataSource={data}
                    rowKey="id"
                    loading={loading}
                    pagination={{
                        current: page + 1,
                        pageSize: 10,
                        total,
                        onChange: (p) => setPage(p - 1)
                    }}
                />
            </Card>
        </div>
    )
}
