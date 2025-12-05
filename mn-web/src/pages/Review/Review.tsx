import { useState, useEffect } from 'react'
import { Card, Button, Empty, Tag, Space, message, Progress } from 'antd'
import { CheckOutlined, CloseOutlined, ReloadOutlined } from '@ant-design/icons'
import { mistakeApi, type Mistake } from '../../api/mistake'

const subjectNames: Record<number, string> = {
    1: '语文', 2: '数学', 3: '英语', 4: '物理', 5: '化学',
    6: '生物', 7: '历史', 8: '地理', 9: '政治',
}

export default function Review() {
    const [mistakes, setMistakes] = useState<Mistake[]>([])
    const [current, setCurrent] = useState(0)
    const [showAnswer, setShowAnswer] = useState(false)
    const [loading, setLoading] = useState(false)
    const [reviewed, setReviewed] = useState(0)
    const [correct, setCorrect] = useState(0)

    const fetchReviewList = async () => {
        setLoading(true)
        try {
            const data = await mistakeApi.getReviewList(1) // TODO: userId
            setMistakes(data)
            setCurrent(0)
            setReviewed(0)
            setCorrect(0)
        } catch (error) {
            message.error('加载失败')
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchReviewList()
    }, [])

    const currentMistake = mistakes[current]

    const handleResult = async (isCorrect: boolean) => {
        if (!currentMistake) return

        try {
            const newStatus = isCorrect ? 'mastered' : 'reviewing'
            const newLevel = isCorrect
                ? Math.min((currentMistake.masteryLevel || 0) + 25, 100)
                : Math.max((currentMistake.masteryLevel || 0) - 10, 0)

            await mistakeApi.updateMastery(currentMistake.id!, newStatus, newLevel)

            setReviewed(r => r + 1)
            if (isCorrect) setCorrect(c => c + 1)

            // 下一题
            if (current < mistakes.length - 1) {
                setCurrent(c => c + 1)
                setShowAnswer(false)
            } else {
                message.success('本轮复习完成！')
            }
        } catch (error) {
            message.error('更新失败')
        }
    }

    if (loading) {
        return <Card loading={true} />
    }

    if (mistakes.length === 0) {
        return (
            <div>
                <div className="page-header">
                    <h2>📖 复习中心</h2>
                </div>
                <Card>
                    <Empty
                        description="暂无需要复习的错题"
                        image={Empty.PRESENTED_IMAGE_SIMPLE}
                    >
                        <Button type="primary" onClick={fetchReviewList}>
                            刷新
                        </Button>
                    </Empty>
                </Card>
            </div>
        )
    }

    const progress = Math.round((reviewed / mistakes.length) * 100)
    const accuracy = reviewed > 0 ? Math.round((correct / reviewed) * 100) : 0

    return (
        <div>
            <div className="page-header">
                <h2>📖 复习中心</h2>
                <Button icon={<ReloadOutlined />} onClick={fetchReviewList}>
                    重新开始
                </Button>
            </div>

            <div className="review-card">
                <h3>今日复习进度</h3>
                <p>待复习 {mistakes.length} 题，已完成 {reviewed} 题</p>
                <Progress
                    percent={progress}
                    strokeColor={{ from: '#667eea', to: '#764ba2' }}
                    style={{ maxWidth: 400, margin: '0 auto' }}
                />
                <p style={{ marginTop: 16 }}>正确率：{accuracy}%</p>
            </div>

            {current < mistakes.length && currentMistake && (
                <Card
                    title={
                        <Space>
                            <Tag color="blue">{subjectNames[currentMistake.subjectId] || '未知科目'}</Tag>
                            <span>第 {current + 1} / {mistakes.length} 题</span>
                        </Space>
                    }
                >
                    <div style={{ fontSize: 16, lineHeight: 1.8, marginBottom: 24 }}>
                        <strong>题目：</strong>
                        <p>{currentMistake.questionContent}</p>
                    </div>

                    {!showAnswer ? (
                        <Button type="primary" onClick={() => setShowAnswer(true)}>
                            显示答案
                        </Button>
                    ) : (
                        <>
                            <div style={{
                                background: '#f6ffed',
                                border: '1px solid #b7eb8f',
                                borderRadius: 8,
                                padding: 16,
                                marginBottom: 16
                            }}>
                                <strong>正确答案：</strong>
                                <p>{currentMistake.correctAnswer || '暂无答案'}</p>
                            </div>

                            {currentMistake.myAnswer && (
                                <div style={{
                                    background: '#fff2f0',
                                    border: '1px solid #ffccc7',
                                    borderRadius: 8,
                                    padding: 16,
                                    marginBottom: 24
                                }}>
                                    <strong>我的答案：</strong>
                                    <p>{currentMistake.myAnswer}</p>
                                </div>
                            )}

                            <Space size="large">
                                <Button
                                    type="primary"
                                    icon={<CheckOutlined />}
                                    onClick={() => handleResult(true)}
                                    style={{ background: '#52c41a' }}
                                >
                                    答对了
                                </Button>
                                <Button
                                    danger
                                    icon={<CloseOutlined />}
                                    onClick={() => handleResult(false)}
                                >
                                    答错了
                                </Button>
                            </Space>
                        </>
                    )}
                </Card>
            )}

            {current >= mistakes.length && (
                <Card>
                    <div style={{ textAlign: 'center', padding: 40 }}>
                        <h2>🎉 复习完成！</h2>
                        <p>本轮复习共 {mistakes.length} 题，正确 {correct} 题</p>
                        <p>正确率：{accuracy}%</p>
                        <Button type="primary" onClick={fetchReviewList} style={{ marginTop: 16 }}>
                            再来一轮
                        </Button>
                    </div>
                </Card>
            )}
        </div>
    )
}
