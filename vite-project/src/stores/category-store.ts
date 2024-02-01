import { defineStore } from 'pinia'
import axiosUtil from '../utils/axios'

const baseUrl = 'http://localhost:8099/api'

const useCategoryStore = defineStore('categoryStore', {
    state: () => ({
        top: [],
        sub: []
    }),
    actions: {
        fetchTop() {
            return axiosUtil.get(`${baseUrl}/testTop`).then(res => {
                this.top = res.data;
            });
        },
        fetchSub(parentId) {
            console.log(parentId)
            return axiosUtil.get(`${baseUrl}/testSub`).then(res => {
                this.sub = res.data;
            });
        },
    },
})

export default useCategoryStore