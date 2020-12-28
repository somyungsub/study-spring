<template>
  <div>
    <h1>Login - Task Agile</h1>
    <button @click="testPost">testPost</button>
    <button @click="testPost2">testPost2</button>
  </div>
</template>

<script>
import testApi from '@/api/test'

export default {
  name: 'LoginPage',
  methods: {
    testPost () {
      const request = {
        id: 'testId',
        password: 'testPW'
      }

      testApi.testPost(request)
        .then(value => {
          console.log(value)
          alert(value)
        })
        .catch(reason => alert(reason))
    },
    testPost2 () {
      const request = {
        id: 'testId2',
        password: 'testPW2'
      }

      testApi.testPost2(request)
        .then(value => {
          console.log(value)
          alert(value)
        })
        .catch(reason => alert(reason))
    }
  }
}
</script>

<style scoped>

</style>
