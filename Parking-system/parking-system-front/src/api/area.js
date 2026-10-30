import request from "@/utils/request";
import { id } from "element-plus/es/locales.mjs";

export const areaListApi = () => request.get('/area/list')
export const areaPageApi = (params) => request.get('/area/page',{ params })
export const areaAddApi = (data) => request.post('/area',data)
export const areaUpdateApi = (data) => request.put('/area',data)
export const areaDeleteApi = (id) => request.delete(`/area/${id}`)