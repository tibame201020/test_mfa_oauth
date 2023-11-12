<script setup lang="ts">
import useAccountStore from './stores/account-store'

const accountStore = useAccountStore();

const clearLogin = () => {
  accountStore.$reset()
}

const login = (account: string) => {
  accountStore.login(account, '123')
}

const getdata = (api: string) => {
  accountStore.sendRequest(api)
}

const broadcastChannel = new BroadcastChannel('sessionStorageChannel');

window.addEventListener('storage', (event) => {
  if (event.key === 'sessionStorageData') {
    broadcastChannel.postMessage(JSON.parse(`${event.newValue}`));
  }
});

broadcastChannel.onmessage = (event) => {
  const newValue = event.data;
  console.log('Received sessionStorage change:', newValue);
};

</script>

<template>
  <div class="container">

    <div class="row">
      <div class="col" v-if="accountStore.user_info">
        {{ accountStore.user_info }}
      </div>
      <div class="col">
        {{ accountStore.res_msg }}
      </div>
    </div>
    <div class="row">
      <div class="col">
        <hr>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <button type="button" class="btn btn-secondary" @click="clearLogin">clear-login</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-secondary" @click="login('test')">login-as-no-role</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-secondary" @click="login('publisher')">login-as-publihser</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-secondary" @click="login('writer')">login-as-writer</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-secondary" @click="login('reader')">login-as-reader</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-secondary" @click="login('all')">login-as-all-role</button>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <hr>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <button type="button" class="btn btn-warning" @click="getdata('/publishApi')">get-open-data</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-warning" @click="getdata('/needAuthApi')">get-auth-data</button>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <hr>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <button type="button" class="btn btn-info" @click="getdata('/publisher/role')">get-publisher-data</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-info" @click="getdata('/writer/role')">get-writer-data</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-info" @click="getdata('/reader/role')">get-reader-data</button>
      </div>
      <div class="col">
        <button type="button" class="btn btn-info"
          @click="getdata('/writerPublisher/role')">get-publisher/writer-data</button>
      </div>
    </div>

  </div>
</template>

<style scoped></style>
