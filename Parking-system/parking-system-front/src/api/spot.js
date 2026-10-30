import request from "@/utils/request";

export const spotAllApi = (params) => request.get('/spot/all', { params })
export const spotStatsApi = () => request.get('/spot/stats')
export const spotPageApi = (params) => request.get('/spot/page', { params })
export const spotAddApi = (data) => request.post('/spot', data)
export const spotUpdateApi = (data) => request.put('/spot', data)
export const spotDeleteApi = (id) => request.delete(`/spot/${id}`)