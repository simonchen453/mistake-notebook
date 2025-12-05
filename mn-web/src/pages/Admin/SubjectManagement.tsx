import { useState, useEffect } from 'react'
import { Card, Table, Button, Modal, Form, Input, Select, Space, message, Popconfirm, Tabs } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import { subjectApi, type Subject, type Chapter, type KnowledgePoint } from '../../api/subject'

export default function SubjectManagement() {
    const [subjects, setSubjects] = useState<Subject[]>([])
    const [chapters, setChapters] = useState<Chapter[]>([])
    const [knowledgePoints, setKnowledgePoints] = useState<KnowledgePoint[]>([])
    const [loading, setLoading] = useState(false)
    const [selectedSubject, setSelectedSubject] = useState<number | null>(null)
    const [selectedChapter, setSelectedChapter] = useState<number | null>(null)

    // Modal states
    const [subjectModal, setSubjectModal] = useState(false)
    const [chapterModal, setChapterModal] = useState(false)
    const [kpModal, setKpModal] = useState(false)
    const [editingItem, setEditingItem] = useState<any>(null)

    const [subjectForm] = Form.useForm()
    const [chapterForm] = Form.useForm()
    const [kpForm] = Form.useForm()

    useEffect(() => {
        fetchSubjects()
    }, [])

    useEffect(() => {
        if (selectedSubject) fetchChapters(selectedSubject)
    }, [selectedSubject])

    useEffect(() => {
        if (selectedChapter) fetchKnowledgePoints(selectedChapter)
    }, [selectedChapter])

    const fetchSubjects = async () => {
        setLoading(true)
        try {
            const data = await subjectApi.listSubjects()
            setSubjects(data)
        } catch (error) {
            message.error('加载科目失败')
        } finally {
            setLoading(false)
        }
    }

    const fetchChapters = async (subjectId: number) => {
        try {
            const data = await subjectApi.listChapters(subjectId)
            setChapters(data)
        } catch (error) {
            message.error('加载章节失败')
        }
    }

    const fetchKnowledgePoints = async (chapterId: number) => {
        try {
            const data = await subjectApi.listKnowledgePoints(chapterId)
            setKnowledgePoints(data)
        } catch (error) {
            message.error('加载知识点失败')
        }
    }

    // Subject handlers
    const handleSaveSubject = async (values: Subject) => {
        try {
            if (editingItem?.id) {
                await subjectApi.updateSubject(editingItem.id, values)
                message.success('更新成功')
            } else {
                await subjectApi.createSubject(values)
                message.success('创建成功')
            }
            setSubjectModal(false)
            fetchSubjects()
        } catch (error) {
            message.error('保存失败')
        }
    }

    const handleDeleteSubject = async (id: number) => {
        try {
            await subjectApi.deleteSubject(id)
            message.success('删除成功')
            fetchSubjects()
        } catch (error) {
            message.error('删除失败')
        }
    }

    // Chapter handlers
    const handleSaveChapter = async (values: Chapter) => {
        try {
            values.subjectId = selectedSubject!
            if (editingItem?.id) {
                await subjectApi.updateChapter(editingItem.id, values)
                message.success('更新成功')
            } else {
                await subjectApi.createChapter(values)
                message.success('创建成功')
            }
            setChapterModal(false)
            fetchChapters(selectedSubject!)
        } catch (error) {
            message.error('保存失败')
        }
    }

    const handleDeleteChapter = async (id: number) => {
        try {
            await subjectApi.deleteChapter(id)
            message.success('删除成功')
            fetchChapters(selectedSubject!)
        } catch (error) {
            message.error('删除失败')
        }
    }

    // KnowledgePoint handlers
    const handleSaveKP = async (values: KnowledgePoint) => {
        try {
            values.chapterId = selectedChapter!
            if (editingItem?.id) {
                await subjectApi.updateKnowledgePoint(editingItem.id, values)
                message.success('更新成功')
            } else {
                await subjectApi.createKnowledgePoint(values)
                message.success('创建成功')
            }
            setKpModal(false)
            fetchKnowledgePoints(selectedChapter!)
        } catch (error) {
            message.error('保存失败')
        }
    }

    return (
        <div>
            <div className="page-header">
                <h2>📚 科目管理</h2>
            </div>

            <div style={{ display: 'flex', gap: 16 }}>
                {/* 科目列表 */}
                <Card title="科目" style={{ width: 300 }} extra={
                    <Button type="primary" size="small" icon={<PlusOutlined />}
                        onClick={() => { setEditingItem(null); subjectForm.resetFields(); setSubjectModal(true) }}>
                        添加
                    </Button>
                }>
                    {subjects.map(s => (
                        <div key={s.id}
                            onClick={() => setSelectedSubject(s.id!)}
                            style={{
                                padding: '8px 12px',
                                cursor: 'pointer',
                                background: selectedSubject === s.id ? '#e6f7ff' : 'transparent',
                                borderRadius: 4,
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center'
                            }}>
                            <span>{s.name}</span>
                            <Space size="small">
                                <EditOutlined onClick={(e) => {
                                    e.stopPropagation()
                                    setEditingItem(s)
                                    subjectForm.setFieldsValue(s)
                                    setSubjectModal(true)
                                }} />
                                <Popconfirm title="确定删除？" onConfirm={() => handleDeleteSubject(s.id!)}>
                                    <DeleteOutlined style={{ color: '#ff4d4f' }} onClick={e => e.stopPropagation()} />
                                </Popconfirm>
                            </Space>
                        </div>
                    ))}
                </Card>

                {/* 章节列表 */}
                <Card title="章节" style={{ width: 350 }} extra={
                    <Button type="primary" size="small" icon={<PlusOutlined />}
                        disabled={!selectedSubject}
                        onClick={() => { setEditingItem(null); chapterForm.resetFields(); setChapterModal(true) }}>
                        添加
                    </Button>
                }>
                    {chapters.map(c => (
                        <div key={c.id}
                            onClick={() => setSelectedChapter(c.id!)}
                            style={{
                                padding: '8px 12px',
                                cursor: 'pointer',
                                background: selectedChapter === c.id ? '#e6f7ff' : 'transparent',
                                borderRadius: 4,
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center'
                            }}>
                            <span>{c.name}</span>
                            <Space size="small">
                                <EditOutlined onClick={(e) => {
                                    e.stopPropagation()
                                    setEditingItem(c)
                                    chapterForm.setFieldsValue(c)
                                    setChapterModal(true)
                                }} />
                                <Popconfirm title="确定删除？" onConfirm={() => handleDeleteChapter(c.id!)}>
                                    <DeleteOutlined style={{ color: '#ff4d4f' }} onClick={e => e.stopPropagation()} />
                                </Popconfirm>
                            </Space>
                        </div>
                    ))}
                    {!selectedSubject && <div style={{ color: '#999', textAlign: 'center' }}>请先选择科目</div>}
                </Card>

                {/* 知识点列表 */}
                <Card title="知识点" style={{ flex: 1 }} extra={
                    <Button type="primary" size="small" icon={<PlusOutlined />}
                        disabled={!selectedChapter}
                        onClick={() => { setEditingItem(null); kpForm.resetFields(); setKpModal(true) }}>
                        添加
                    </Button>
                }>
                    <Table
                        dataSource={knowledgePoints}
                        rowKey="id"
                        size="small"
                        columns={[
                            { title: '知识点', dataIndex: 'name' },
                            { title: '描述', dataIndex: 'description', ellipsis: true },
                            {
                                title: '操作',
                                width: 100,
                                render: (_, record) => (
                                    <Space>
                                        <EditOutlined onClick={() => {
                                            setEditingItem(record)
                                            kpForm.setFieldsValue(record)
                                            setKpModal(true)
                                        }} />
                                        <Popconfirm title="确定删除？" onConfirm={async () => {
                                            await subjectApi.deleteKnowledgePoint(record.id!)
                                            fetchKnowledgePoints(selectedChapter!)
                                        }}>
                                            <DeleteOutlined style={{ color: '#ff4d4f' }} />
                                        </Popconfirm>
                                    </Space>
                                )
                            }
                        ]}
                    />
                    {!selectedChapter && <div style={{ color: '#999', textAlign: 'center' }}>请先选择章节</div>}
                </Card>
            </div>

            {/* Modals */}
            <Modal title={editingItem ? '编辑科目' : '添加科目'} open={subjectModal}
                onCancel={() => setSubjectModal(false)} onOk={() => subjectForm.submit()}>
                <Form form={subjectForm} onFinish={handleSaveSubject} layout="vertical">
                    <Form.Item name="name" label="科目名称" rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>
                    <Form.Item name="sortOrder" label="排序">
                        <Input type="number" />
                    </Form.Item>
                </Form>
            </Modal>

            <Modal title={editingItem ? '编辑章节' : '添加章节'} open={chapterModal}
                onCancel={() => setChapterModal(false)} onOk={() => chapterForm.submit()}>
                <Form form={chapterForm} onFinish={handleSaveChapter} layout="vertical">
                    <Form.Item name="name" label="章节名称" rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>
                    <Form.Item name="sortOrder" label="排序">
                        <Input type="number" />
                    </Form.Item>
                </Form>
            </Modal>

            <Modal title={editingItem ? '编辑知识点' : '添加知识点'} open={kpModal}
                onCancel={() => setKpModal(false)} onOk={() => kpForm.submit()}>
                <Form form={kpForm} onFinish={handleSaveKP} layout="vertical">
                    <Form.Item name="name" label="知识点名称" rules={[{ required: true }]}>
                        <Input />
                    </Form.Item>
                    <Form.Item name="description" label="描述">
                        <Input.TextArea rows={3} />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    )
}
