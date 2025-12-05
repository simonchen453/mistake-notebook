import { useState, useRef, useEffect } from 'react'
import { Card, Input, Button, message, Spin } from 'antd'
import { SendOutlined, RobotOutlined, UserOutlined } from '@ant-design/icons'
import ReactMarkdown from 'react-markdown'
import { aiApi } from '../../api/ai'

const { TextArea } = Input

interface Message {
    role: 'user' | 'model'
    content: string
}

export default function AIChat() {
    const [messages, setMessages] = useState<Message[]>([
        {
            role: 'model',
            content: '你好！我是错题本 AI 助手「小错」🤖\n\n我可以帮你：\n- 📝 分析错题原因\n- 💡 讲解解题思路\n- 📚 推荐相似练习题\n- 🎯 制定学习计划\n\n有什么可以帮你的吗？'
        }
    ])
    const [input, setInput] = useState('')
    const [loading, setLoading] = useState(false)
    const messagesEndRef = useRef<HTMLDivElement>(null)

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' })
    }

    useEffect(() => {
        scrollToBottom()
    }, [messages])

    const handleSend = async () => {
        if (!input.trim()) return

        const userMessage = input.trim()
        setInput('')

        setMessages(prev => [...prev, { role: 'user', content: userMessage }])
        setLoading(true)

        try {
            const history = messages.map(m => ({
                role: m.role,
                content: m.content
            }))

            const result = await aiApi.chat(userMessage, history)

            setMessages(prev => [...prev, {
                role: 'model',
                content: result.response
            }])
        } catch (error) {
            message.error('发送失败，请重试')
            // 添加错误消息
            setMessages(prev => [...prev, {
                role: 'model',
                content: '抱歉，出现了一些问题，请稍后重试。'
            }])
        } finally {
            setLoading(false)
        }
    }

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault()
            handleSend()
        }
    }

    return (
        <div>
            <div className="page-header">
                <h2>🤖 AI 学习助手</h2>
            </div>

            <Card className="chat-container">
                <div className="chat-messages">
                    {messages.map((msg, index) => (
                        <div key={index} className={`chat-message ${msg.role === 'user' ? 'user' : 'ai'}`}>
                            <div className="avatar">
                                {msg.role === 'user' ? <UserOutlined /> : <RobotOutlined />}
                            </div>
                            <div className="content">
                                {msg.role === 'model' ? (
                                    <ReactMarkdown>{msg.content}</ReactMarkdown>
                                ) : (
                                    msg.content
                                )}
                            </div>
                        </div>
                    ))}
                    {loading && (
                        <div className="chat-message ai">
                            <div className="avatar">
                                <RobotOutlined />
                            </div>
                            <div className="content">
                                <Spin size="small" /> 思考中...
                            </div>
                        </div>
                    )}
                    <div ref={messagesEndRef} />
                </div>

                <div className="chat-input-area">
                    <TextArea
                        value={input}
                        onChange={e => setInput(e.target.value)}
                        onKeyPress={handleKeyPress}
                        placeholder="输入你的问题，按 Enter 发送..."
                        autoSize={{ minRows: 2, maxRows: 4 }}
                        disabled={loading}
                    />
                    <Button
                        type="primary"
                        icon={<SendOutlined />}
                        onClick={handleSend}
                        loading={loading}
                        style={{ height: 'auto', minHeight: 54 }}
                    >
                        发送
                    </Button>
                </div>
            </Card>
        </div>
    )
}
