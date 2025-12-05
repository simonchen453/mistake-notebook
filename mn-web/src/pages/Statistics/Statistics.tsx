import { useState, useEffect } from 'react'
import { Card, Row, Col, Statistic, message } from 'antd'
import {
    BookOutlined, CheckCircleOutlined, ClockCircleOutlined,
    ExclamationCircleOutlined
} from '@ant-design/icons'
import { mistakeApi } from '../../api/mistake'

const subjectNames: Record<number, string> = {
    1: '语文', 2: '数学', 3: '英语', 4: '物理', 5: '化学',
    6: '生物', 7: '历史', 8: '地理', 9: '政治',
}

export default function Statistics() {
    const [loading, setLoading] = useState(false)
    const [stats, setStats] = useState<{
        bySubject: [number, number][]
        byErrorReason: [string, number][]
    }>({ bySubject: [], byErrorReason: [] })

    useEffect(() => {
        fetchStats()
    }, [])

    const fetchStats = async () => {
        setLoading(true)
        try {
            const data = await mistakeApi.getStats(1) // TODO: userId
            setStats(data)
        } catch (error) {
            message.error('加载统计数据失败')
        } finally {
            setLoading(false)
        }
    }

    const totalMistakes = stats.bySubject.reduce((sum, [, count]) => sum + count, 0)

    // 计算最薄弱科目
    const weakestSubject = stats.bySubject.length > 0
        ? stats.bySubject.reduce((max, curr) => curr[1] > max[1] ? curr : max)
        : null

    // 计算最常见错误原因
    const topErrorReason = stats.byErrorReason.length > 0
        ? stats.byErrorReason.reduce((max, curr) => curr[1] > max[1] ? curr : max)
        : null

    return (
        <div>
            <div className="page-header">
                <h2>📊 统计分析</h2>
            </div>

            <Row gutter={16}>
                <Col span={6}>
                    <Card loading={loading}>
                        <Statistic
                            title="总错题数"
                            value={totalMistakes}
                            prefix={<BookOutlined />}
                            valueStyle={{ color: '#1890ff' }}
                        />
                    </Card>
                </Col>
                <Col span={6}>
                    <Card loading={loading}>
                        <Statistic
                            title="涉及科目"
                            value={stats.bySubject.length}
                            prefix={<BookOutlined />}
                            valueStyle={{ color: '#52c41a' }}
                        />
                    </Card>
                </Col>
                <Col span={6}>
                    <Card loading={loading}>
                        <Statistic
                            title="最薄弱科目"
                            value={weakestSubject ? subjectNames[weakestSubject[0]] || '未知' : '-'}
                            prefix={<ExclamationCircleOutlined />}
                            valueStyle={{ color: '#faad14' }}
                        />
                    </Card>
                </Col>
                <Col span={6}>
                    <Card loading={loading}>
                        <Statistic
                            title="最常见错因"
                            value={topErrorReason ? topErrorReason[0] : '-'}
                            prefix={<ClockCircleOutlined />}
                            valueStyle={{ color: '#ff4d4f' }}
                        />
                    </Card>
                </Col>
            </Row>

            <Row gutter={16} style={{ marginTop: 16 }}>
                <Col span={12}>
                    <Card title="各科目错题分布" loading={loading}>
                        {stats.bySubject.length > 0 ? (
                            <div>
                                {stats.bySubject.map(([subjectId, count]) => (
                                    <div
                                        key={subjectId}
                                        style={{
                                            display: 'flex',
                                            justifyContent: 'space-between',
                                            padding: '8px 0',
                                            borderBottom: '1px solid #f0f0f0'
                                        }}
                                    >
                                        <span>{subjectNames[subjectId] || `科目${subjectId}`}</span>
                                        <span style={{ fontWeight: 600 }}>{count} 题</span>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div style={{ textAlign: 'center', color: '#999', padding: 40 }}>
                                暂无数据
                            </div>
                        )}
                    </Card>
                </Col>
                <Col span={12}>
                    <Card title="错误原因分布" loading={loading}>
                        {stats.byErrorReason.length > 0 ? (
                            <div>
                                {stats.byErrorReason.map(([reason, count]) => (
                                    <div
                                        key={reason}
                                        style={{
                                            display: 'flex',
                                            justifyContent: 'space-between',
                                            padding: '8px 0',
                                            borderBottom: '1px solid #f0f0f0'
                                        }}
                                    >
                                        <span>{reason}</span>
                                        <span style={{ fontWeight: 600 }}>{count} 题</span>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div style={{ textAlign: 'center', color: '#999', padding: 40 }}>
                                暂无数据
                            </div>
                        )}
                    </Card>
                </Col>
            </Row>
        </div>
    )
}
