<template>
  <div>
    <ServiceHeader></ServiceHeader>
    <div class="flex">
      <ServiceNavbar></ServiceNavbar>

      <!-- NOTICE -->

      <div class="w-screen">
        <div class="flex flex-col space-y-10">
          <!-- 
        # 네이버님 안녕하세요
        -->
          <nav class="flex flex-wrap justify-between p-8 text-gray-800">
            <h1 class="font-bold">{{ serviceUserName }} 님, 안녕하세요</h1>
            <h3 class="font-bold text-gray-500">DASHBOARD</h3>
          </nav>
          <div class="flex flex-col">
            <div class="flex flex-col justify-center">
              <div class="inline-block w-auto py-2 space-y-4 sm:px-6 lg:px-8">
                <div class="flex flex-wrap justify-between px-5">
                  <p class="text-2xl font-medium">공지 게시판</p>
                  <div class="flex justify-center space-x-2">
                    <button
                      @click="moveToNoticeCreate()"
                      type="button"
                      class="inline-block rounded bg-blue-600 px-6 py-2.5 text-md font-medium uppercase leading-tight text-white shadow-md transition duration-150 ease-in-out hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg"
                    >
                      공지 작성
                    </button>
                  </div>
                </div>
                <div class="overflow-hidden rounded-md shadow-lg">
                  <table class="min-w-full">
                    <thead class="bg-white border-b">
                      <tr>
                        <th
                          scope="col"
                          class="w-1/6 px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          카테고리
                        </th>
                        <th
                          scope="col"
                          class="w-4/6 px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          공지명
                        </th>
                        <th
                          scope="col"
                          class="px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          날짜
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr
                        v-for="(item, index) in boardList.slice().reverse()"
                        :content="item.value"
                        :key="index"
                        class="transition duration-300 ease-in-out bg-white border-b hover:bg-gray-100"
                      >
                        <td
                          class="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap"
                        >
                          {{ index + 1 }}
                        </td>
                        <td
                          class="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap"
                        >
                          <a @click="goDetail(item.id)" :data="item">
                            {{ item.title }}
                          </a>
                        </td>
                        <td
                          class="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap"
                        >
                          {{ item.createdAt.slice(0, 10) }}
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import ServiceHeader from '@/components/ServiceHeader.vue';
import ServiceNavbar from '@/components/ServiceNavbar.vue';

import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

defineEmits(['update:compnotice']);

const serviceUserName = ref('');
const getServiceUserName = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  axios
    .get(`${BASE_URL}/serviceusers/me`, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    })
    .then(res => {
      console.log(res.data);
      serviceUserName.value = res.data.name;
    })
    .catch(err => {
      console.log(err.message);
    });
};
const boardList = ref([]);
// const BASE_URL = 'http://localhost:8082/api';

// const notices = ref(null);
// const getCorporateNotices = () => {
//   const user = JSON.parse(localStorage.getItem('user'));
//   axios
//     .get(`${BASE_URL}/serviceusers/notices`, {
//       Authoriation: `Bearer ${user.accessToken}`,
//     })
//     .then(res => {
//       console.log('res.data: ', res.data);
//     });
// // };
// const currentComp = shallowRef();
// const changeCurrentComp = comp => (currentComp.value = comp);

onMounted(() => {
  getContextList();
  getServiceUserName();
});
const list = [];
const BASE_URL = 'https://i8a106.p.ssafy.io/api';
const user = JSON.parse(localStorage.getItem('user'));

const getContextList = () => {
  axios
    .get(`${BASE_URL}/serviceusers/notices`, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    })
    .then(data => {
      console.log(data.data.content);
      boardList.value = data.data.content;
    })
    .catch(err => {
      console.log(err);
    });
};

const router = useRouter();
const moveToNoticeCreate = () => {
  router.push({ name: 'ServiceNoticeCreate' });
};
const goDetail = serviceNoticeId => {
  router.push({
    name: 'ServiceNoticeDetail',
    params: { id: serviceNoticeId },
  });
};
</script>

<style lang="scss" scoped></style>
