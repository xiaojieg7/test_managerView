export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  username: string
  realName: string
  role: 'ADMIN' | 'REPAIRER' | 'OWNER'
  userId: number
  ownerId?: number
  permissions: string[]
}

export interface ApiResponse<T = any> {
  code: number
  msg: string
  data: T
}

export interface PageResult<T> {
  total: number
  records: T[]
  current: number
  size: number
  pages: number
}

export interface User {
  id: number
  username: string
  realName?: string
  phone?: string
  email?: string
  role: string
  status: number
  createTime?: string
  updateTime?: string
}

export interface Owner {
  id: number
  userId: number
  username?: string
  realName?: string
  phone?: string
  email?: string
  idCard?: string
  building?: string
  unit?: string
  roomNumber?: string
  checkInDate?: string
  status?: number
  createTime?: string
}

export interface Repair {
  id: number
  ownerId: number
  repairerId?: number
  title: string
  description?: string
  category?: string
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'CANCELLED'
  priority?: 'LOW' | 'NORMAL' | 'HIGH' | 'URGENT'
  repairRemark?: string
  completeTime?: string
  createTime?: string
  ownerName?: string
  ownerBuilding?: string
  ownerUnit?: string
  ownerRoomNumber?: string
  repairerName?: string
}

export interface Payment {
  id: number
  ownerId: number
  paymentType: 'PROPERTY_FEE' | 'WATER' | 'ELECTRIC' | 'GAS'
  amount: number
  period?: string
  status: 'UNPAID' | 'PAID' | 'OVERDUE'
  dueDate?: string
  paidTime?: string
  paymentMethod?: string
  remark?: string
  createTime?: string
  ownerName?: string
  ownerBuilding?: string
  ownerUnit?: string
  ownerRoomNumber?: string
}

export interface Notice {
  id: number
  title: string
  content?: string
  type: 'NOTICE' | 'ACTIVITY' | 'SYSTEM'
  priority?: number
  publisherId?: number
  publishTime?: string
  status?: string
  createTime?: string
  publisherName?: string
}

export interface Building {
  id: number
  buildingName: string
  buildingCode: string
  unitCount?: number
  floorCount?: number
  description?: string
  status?: number
  createTime?: string
}

export interface DashboardStats {
  totalUsers: number
  totalOwners: number
  totalRepairers: number
  pendingRepairs: number
  totalRepairs: number
}

export interface MenuItem {
  path: string
  name: string
  icon?: string
  children?: MenuItem[]
}
