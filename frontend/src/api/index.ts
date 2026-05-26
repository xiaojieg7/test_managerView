import request from '@/utils/request'
import type { ApiResponse, LoginRequest, LoginResponse } from '@/types'

export const authApi = {
  login: (data: LoginRequest) => {
    return request.post<any, ApiResponse<LoginResponse>>('/auth/login', data)
  },
  logout: () => {
    return request.post<any, ApiResponse<void>>('/auth/logout')
  }
}

export const userApi = {
  getList: () => {
    return request.get<any, ApiResponse<any[]>>('/admin/users')
  },
  getById: (id: number) => {
    return request.get<any, ApiResponse<any>>(`/admin/users/${id}`)
  },
  create: (data: any) => {
    return request.post<any, ApiResponse<any>>('/admin/users', data)
  },
  update: (id: number, data: any) => {
    return request.put<any, ApiResponse<any>>(`/admin/users/${id}`, data)
  },
  delete: (id: number) => {
    return request.delete<any, ApiResponse<void>>(`/admin/users/${id}`)
  },
  updateStatus: (id: number, status: number) => {
    return request.put<any, ApiResponse<void>>(`/admin/users/${id}/status`, null, {
      params: { status }
    })
  }
}

export const ownerApi = {
  getPage: (params: any) => {
    return request.get<any, ApiResponse<any>>('/admin/owners', { params })
  },
  getAll: () => {
    return request.get<any, ApiResponse<any[]>>('/admin/owners/all')
  },
  getById: (id: number) => {
    return request.get<any, ApiResponse<any>>(`/admin/owners/${id}`)
  },
  create: (data: any) => {
    return request.post<any, ApiResponse<any>>('/admin/owners', data)
  },
  update: (id: number, data: any) => {
    return request.put<any, ApiResponse<any>>(`/admin/owners/${id}`, data)
  },
  delete: (id: number) => {
    return request.delete<any, ApiResponse<void>>(`/admin/owners/${id}`)
  }
}

export const repairApi = {
  getPage: (params: any) => {
    return request.get<any, ApiResponse<any>>('/repair', { params })
  },
  getById: (id: number) => {
    return request.get<any, ApiResponse<any>>(`/repair/${id}`)
  },
  create: (data: any) => {
    return request.post<any, ApiResponse<any>>('/repair', data)
  },
  update: (id: number, data: any) => {
    return request.put<any, ApiResponse<any>>(`/repair/${id}`, data)
  },
  assign: (id: number, repairerId: number) => {
    return request.put<any, ApiResponse<any>>(`/repair/${id}/assign`, null, {
      params: { repairerId }
    })
  },
  updateStatus: (id: number, status: string, remark?: string) => {
    return request.put<any, ApiResponse<any>>(`/repair/${id}/status`, null, {
      params: { status, remark }
    })
  },
  delete: (id: number) => {
    return request.delete<any, ApiResponse<void>>(`/repair/${id}`)
  },
  getMyRepairs: () => {
    return request.get<any, ApiResponse<any[]>>('/repair/my')
  },
  getAssignedRepairs: () => {
    return request.get<any, ApiResponse<any[]>>('/repair/assigned')
  }
}

export const paymentApi = {
  getPage: (params: any) => {
    return request.get<any, ApiResponse<any>>('/payment', { params })
  },
  getById: (id: number) => {
    return request.get<any, ApiResponse<any>>(`/payment/${id}`)
  },
  create: (data: any) => {
    return request.post<any, ApiResponse<any>>('/payment', data)
  },
  update: (id: number, data: any) => {
    return request.put<any, ApiResponse<any>>(`/payment/${id}`, data)
  },
  pay: (id: number, paymentMethod: string) => {
    return request.post<any, ApiResponse<any>>(`/payment/${id}/pay`, null, {
      params: { paymentMethod }
    })
  },
  delete: (id: number) => {
    return request.delete<any, ApiResponse<void>>(`/payment/${id}`)
  },
  getMyPayments: (params: any) => {
    return request.get<any, ApiResponse<any>>('/payment/my', { params })
  },
  getMyUnpaid: () => {
    return request.get<any, ApiResponse<any>>('/payment/my/unpaid')
  }
}

export const noticeApi = {
  getPage: (params: any) => {
    return request.get<any, ApiResponse<any>>('/notice', { params })
  },
  getById: (id: number) => {
    return request.get<any, ApiResponse<any>>(`/notice/${id}`)
  },
  create: (data: any) => {
    return request.post<any, ApiResponse<any>>('/notice', data)
  },
  update: (id: number, data: any) => {
    return request.put<any, ApiResponse<any>>(`/notice/${id}`, data)
  },
  delete: (id: number) => {
    return request.delete<any, ApiResponse<void>>(`/notice/${id}`)
  }
}

export const buildingApi = {
  getAll: () => {
    return request.get<any, ApiResponse<any[]>>('/building/all')
  },
  getById: (id: number) => {
    return request.get<any, ApiResponse<any>>(`/building/${id}`)
  },
  create: (data: any) => {
    return request.post<any, ApiResponse<any>>('/building', data)
  },
  update: (id: number, data: any) => {
    return request.put<any, ApiResponse<any>>(`/building/${id}`, data)
  },
  delete: (id: number) => {
    return request.delete<any, ApiResponse<void>>(`/building/${id}`)
  }
}

export const dashboardApi = {
  getStats: () => {
    return request.get<any, ApiResponse<any>>('/dashboard/stats')
  }
}
