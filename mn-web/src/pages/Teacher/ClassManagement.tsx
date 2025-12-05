import { useState, useEffect } from 'react'
import { Card, Table, Button, Modal, Form, Input, Select, Space, message, Popconfirm, Tag, Tabs } from 'antd'
import { PlusOutlined, DeleteOutlined, UserAddOutlined } from '@ant-design/icons'
import { classroomApi, type Classroom, type StudentClass } from '../../api/classroom'

export default function ClassManagement() {
    const [classrooms, setClassrooms] = useState<Classroom[]>([])
    const [students, setStudents] = useState<StudentClass[]>([])
    const [loading, setLoading] = useState(false)
    const [selectedClass, setSelectedClass] = useState<number | null>(null)
    const [classModal, setClassModal] = useState(false)
    const [studentModal, setStudentModal] = useState(false)
    const [form] = Form.useForm()
    const [studentForm] = Form.useForm()

    // TODO: 从登录用户获取 teacherId
    const teacherId = 1

    useEffect(() => {
        fetchClassrooms()
    }, [])

    useEffect(() => {
        if (selectedClass) fetchStudents(selectedClass)
    }, [selectedClass])

    const fetchClassrooms = async () => {
        setLoading(true)
        try {
            const data = await classroomApi.list(teacherId)
            setClassrooms(data)
        } catch (error) {
            message.error('加载班级失败')
        } finally {
            setLoading(false)
        }
    }

    const fetchStudents = async (classId: number) => {
        try {
            const data = await classroomApi.listStudents(classId)
            setStudents(data)
        } catch (error) {
            message.error('加载学生列表失败')
        }
    }

    const handleSaveClass = async (values: Classroom) => {
        try {
            values.teacherId = teacherId
            await classroomApi.create(values)
            message.success('创建成功')
            setClassModal(false)
            fetchClassrooms()
        } catch (error) {
            message.error('创建失败')
        }
    }

    const handleDeleteClass = async (id: number) => {
        try {
            await classroomApi.delete(id)
            message.success('删除成功')
            fetchClassrooms()
            if (selectedClass === id) {
                setSelectedClass(null)
                setStudents([])
            }
        } catch (error) {
            message.error('删除失败')
        }
    }

    const handleAddStudent = async (values: { studentId: number; studentNo?: string }) => {
        try {
            await classroomApi.addStudent(selectedClass!, values.studentId, values.studentNo)
            message.success('添加成功')
            setStudentModal(false)
            fetchStudents(selectedClass!)
        } catch (error: any) {
            message.error(error.response?.data?.error || '添加失败')
        }
    }

    const handleRemoveStudent = async (studentId: number) => {
        try {
            await classroomApi.removeStudent(selectedClass!, studentId)
            message.success('移除成功')
            fetchStudents(selectedClass!)
        } catch (error) {
            message.error('移除失败')
        }
    }

    const gradeOptions = [
        { value: '七年级', label: '七年级' },
        { value: '八年级', label: '八年级' },
        { value: '九年级', label: '九年级' },
    ]

    return (
        <div>
            <div className="page-header">
                <h2>🏫 班级管理</h2>
                <Button type="primary" icon={<PlusOutlined />} onClick={() => {
                    form.resetFields()
                    setClassModal(true)
                }}>
                    创建班级
                </Button>
            </div>

            <div style={{ display: 'flex', gap: 16 }}>
                {/* 班级列表 */}
                <Card title="我的班级" style={{ width: 350 }} loading={loading}>
                    {classrooms.length === 0 ? (
                        <div style={{ textAlign: 'center', color: '#999', padding: 40 }}>
                            暂无班级，点击右上角创建
                        </div>
                    ) : (
                        classrooms.map(c => (
                            <div key={c.id}
                                onClick={() => setSelectedClass(c.id!)}
                                style={{
                                    padding: 12,
                                    border: '1px solid #f0f0f0',
                                    borderRadius: 8,
                                    marginBottom: 8,
                                    cursor: 'pointer',
                                    background: selectedClass === c.id ? '#e6f7ff' : 'white',
                                }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                    <div>
                                        <div style={{ fontWeight: 500 }}>{c.name}</div>
                                        <div style={{ fontSize: 12, color: '#999' }}>
                                            <Tag color="blue">{c.grade}</Tag>
                                            {c.schoolName}
                                        </div>
                                    </div>
                                    <Popconfirm title="确定删除此班级？" onConfirm={() => handleDeleteClass(c.id!)}>
                                        <DeleteOutlined style={{ color: '#ff4d4f' }} onClick={e => e.stopPropagation()} />
                                    </Popconfirm>
                                </div>
                            </div>
                        ))
                    )}
                </Card>

                {/* 学生列表 */}
                <Card title="班级学生" style={{ flex: 1 }} extra={
                    <Button type="primary" size="small" icon={<UserAddOutlined />}
                        disabled={!selectedClass}
                        onClick={() => { studentForm.resetFields(); setStudentModal(true) }}>
                        添加学生
                    </Button>
                }>
                    {selectedClass ? (
                        <Table
                            dataSource={students}
                            rowKey="id"
                            columns={[
                                { title: '学生ID', dataIndex: 'studentId' },
                                { title: '学号', dataIndex: 'studentNo' },
                                {
                                    title: '加入时间', dataIndex: 'joinedAt',
                                    render: (v: string) => v ? new Date(v).toLocaleDateString() : '-'
                                },
                                {
                                    title: '操作',
                                    width: 100,
                                    render: (_, record) => (
                                        <Popconfirm title="确定移除此学生？" onConfirm={() => handleRemoveStudent(record.studentId)}>
                                            <Button type="link" danger size="small">移除</Button>
                                        </Popconfirm>
                                    )
                                }
                            ]}
                        />
                    ) : (
                        <div style={{ textAlign: 'center', color: '#999', padding: 60 }}>
                            请选择一个班级
                        </div>
                    )}
                </Card>
            </div>

            {/* 创建班级 Modal */}
            <Modal title="创建班级" open={classModal} onCancel={() => setClassModal(false)} onOk={() => form.submit()}>
                <Form form={form} onFinish={handleSaveClass} layout="vertical">
                    <Form.Item name="name" label="班级名称" rules={[{ required: true }]}>
                        <Input placeholder="如：初一(3)班" />
                    </Form.Item>
                    <Form.Item name="grade" label="年级" rules={[{ required: true }]}>
                        <Select options={gradeOptions} placeholder="选择年级" />
                    </Form.Item>
                    <Form.Item name="schoolName" label="学校名称">
                        <Input placeholder="可选" />
                    </Form.Item>
                </Form>
            </Modal>

            {/* 添加学生 Modal */}
            <Modal title="添加学生" open={studentModal} onCancel={() => setStudentModal(false)} onOk={() => studentForm.submit()}>
                <Form form={studentForm} onFinish={handleAddStudent} layout="vertical">
                    <Form.Item name="studentId" label="学生用户ID" rules={[{ required: true }]}>
                        <Input type="number" placeholder="输入学生的用户ID" />
                    </Form.Item>
                    <Form.Item name="studentNo" label="学号">
                        <Input placeholder="可选" />
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    )
}
