import { defineStore } from 'pinia'
import axiosUtil from '../utils/axios'

const baseUrl = 'http://localhost:8099/api'

const useAccountStore = defineStore('accountStore', {
    state: () => ({
        user_info: {},
        access_token: '',
        res_msg: '',
        loading: false,
    }),
    actions: {
        login(account: string, password: string): void {
            axiosUtil.post(`${baseUrl}/login`, {
                account: account,
                password: password
            }, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            })
                .then(res => {
                    console.log(res)
                    const accessToken = res.headers['access_token'];
                    console.log('store: ' + accessToken)
                    this.access_token = accessToken;
                    this.user_info = res.data
                })
        },
        sendRequest(api: string): void {
            axiosUtil.post(`${baseUrl}${api}`)
                .then(res => {
                    console.log(res)
                    this.res_msg = res.data
                })
        }
    },
    persist: {
        storage: false
    },
})

export default useAccountStore