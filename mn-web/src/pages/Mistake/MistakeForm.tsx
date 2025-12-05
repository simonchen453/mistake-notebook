import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import {
    Card, Form, Input, Select, Button, Space, message, Rate, Radio
} from 'antd'
import { ArrowLeftOutlined, SaveOutlined } from '@ant-design/icons'
import { mistakeApi, type Mistake } from '../../api/mistake'

const { TextArea } = Input

const subjectOptions = [
    { value: 1, label: '语文' },
    { value: 2, label: '数学' },
    { value: 3, label: '英语' },
    { value: 4, label: '物理' },
    { value: 5, label: '化学' },
    { value: 6, label: '生物' },
    { value: 7, label: '历史' },
    { value: 8, label: '地理' },
    { value: 9, label: '政治' },
]

const errorReasonOptions = [
    { value: '粗心大意', label: '粗心大意' },
    { value: '概念不清', label: '概念不清' },
    { value: '方法错误', label: '方法错误' },
    { value: '审题不清', label: '审题不清' },
    { value: '未掌握', label: '未掌握' },
]

const sourceOptions = [
    { value: '考试', label: '考试' },
    { value: '作业', label: '作业' },
    { value: '练习', label: '练习' },
]

export default function MistakeForm() {
    const navigate = useNavigate()
    const { id } = useParams()
    const [form] = Form.useForm()
    const [loading, setLoading] = useState(false)
    const [saving, setSaving] = useState(false)

    const isEdit = !!id

    useEffect(() => {
        if (id) {
            fetchDetail()
        }
    }, [id])

    const fetchDetail = async () => {
        setLoading(true)
        try {
            const data = await mistakeApi.getById(Number(id))
            form.setFieldsValue(data)
        } catch (error) {
            message.error('加载失败')
        } finally {
            setLoading(false)
        }
    }

    const handleSubmit = async (values: Mistake) => {
        setSaving(true)
        try {
            // TODO: 从登录用户获取 userId
            values.userId = 1

            if (isEdit) {
                await mistakeApi.update(Number(id), values)
                message.success('更新成功')
            } else {
                await mistakeApi.create(values)
                message.success('创建成功')
            }
            navigate('/mistakes')
        } catch (error) {
            message.error(isEdit ? '更新失败' : '创建失败')
        } finally {
            setSaving(false)
        }
    }

    return (
        <div>
            <div className="page-header">
                <Space>
                    <Button
                        icon={<ArrowLeftOutlined />}
                        onClick={() => navigate('/mistakes')}
                    >
                        返回
                    </Button>
                    <h2>{isEdit ? '编辑错题' : '添加错题'}</h2>
                </Space>
            </div>

            <Card loading={loading}>
                <Form
                    form={form}
                    layout="vertical"
                    onFinish={handleSubmit}
                    initialValues={{
                        difficulty: 3,
                        masteryStatus: 'not_mastered',
                        source: '作业'
                    }}
                    style={{ maxWidth: 800 }}
                >
                    <Form.Item
                        name="subjectId"
                        label="科目"
                        rules={[{ required: true, message: '请选择科目' }]}
                    >
                        <Select options={subjectOptions} placeholder="选择科目" />
                    </Form.Item>

                    <Form.Item
                        name="questionContent"
                        label="题目内容"
                        rules={[{ required: true, message: '请输入题目内容' }]}
                    >
                        <TextArea
                            rows={4}
                            placeholder="输入题目内容，支持粘贴图片..."
                        />
                    </Form.Item>

                    <Form.Item name="correctAnswer" label="正确答案">
                        <TextArea rows={3} placeholder="输入正确答案" />
                    </Form.Item>

                    <Form.Item name="myAnswer" label="我的答案">
                        <TextArea rows={3} placeholder="输入你的答案" />
                    </Form.Item>

                    <Form.Item name="errorReason" label="错误原因">
                        <Select
                            options={errorReasonOptions}
                            placeholder="选择错误原因"
                            allowClear
                        />
                    </Form.Item>

                    <Form.Item name="errorReasonDetail" label="详细原因">
                        <TextArea rows={2} placeholder="详细描述错误原因..." />
                    </Form.Item>

                    <Form.Item name="difficulty" label="难度">
                        <Rate />
                    </Form.Item>

                    <Form.Item name="source" label="来源">
                        <Radio.Group options={sourceOptions} />
                    </Form.Item>

                    <Form.Item name="sourceName" label="来源名称">
                        <Input placeholder="如：期中考试、第三章练习..." />
                    </Form.Item>

                    <Form.Item name="masteryStatus" label="掌握状态">
                        <Radio.Group>
                            <Radio value="not_mastered">未掌握</Radio>
                            <Radio value="reviewing">复习中</Radio>
                            <Radio value="mastered">已掌握</Radio>
                        </Radio.Group>
                    </Form.Item>

                    <Form.Item>
                        <Space>
                            <Button
                                type="primary"
                                htmlType="submit"
                                icon={<SaveOutlined />}
                                loading={saving}
                            >
                                保存
                            </Button>
                            <Button onClick={() => navigate('/mistakes')}>
                                取消
                            </Button>
                        </Space>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    )
}
