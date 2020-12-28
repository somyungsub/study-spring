import axios from 'axios'

const testApi = {
  testPost (request) {
    return axios.post('/api/testPost', request)
  },
  testPost2 (request) {
    return axios.post('/testPost', request)
  }
}

export default testApi
