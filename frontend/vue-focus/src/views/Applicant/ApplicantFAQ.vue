<template>
  <div>
    <ApplicantHeader></ApplicantHeader>
    <div class="flex">
      <ApplicantNavbar></ApplicantNavbar>
      <div class="w-screen">
        <div class="flex flex-col space-y-10">
          <!-- 
    # 네이버님 안녕하세요
    -->
          <nav class="flex flex-wrap justify-between p-8 text-gray-800">
            <h1 class="font-bold">{{ applicantsName }} 님, 안녕하세요</h1>
            <h3 class="font-bold text-gray-500">FAQ</h3>
          </nav>
          <div class="flex flex-col">
            <div class="flex flex-col justify-center">
              <div class="inline-block w-auto py-2 space-y-4 sm:px-6 lg:px-8">
                <div class="flex flex-wrap justify-between px-5">
                  <p class="text-2xl font-medium">FAQ</p>
                </div>
                <div class="overflow-hidden rounded-md shadow-lg">
                  <table class="min-w-full">
                    <thead class="bg-white border-b">
                      <tr>
                        <th
                          scope="col"
                          class="px-6 py-4 text-sm font-medium text-left text-gray-900"
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
                          <a href="">
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
import { ref, onMounted, shallowRef } from 'vue';
import axios from 'axios';

import ApplicantHeader from '@/components/ApplicantHeader.vue';
import ApplicantNavbar from '@/components/ApplicantNavbar.vue';

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
  getapplicantsName();
});
const list = [];
const BASE_URL = 'https://i8a106.p.ssafy.io/api';

const applicantsName = ref('');
const getapplicantsName = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  axios
    .get(`${BASE_URL}/applicants/me`, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    })
    .then(res => {
      console.log(res.data);
      applicantsName.value = res.data.name;
    })
    .catch(err => {
      console.log(err.message);
    });
};
const user = JSON.parse(localStorage.getItem('user'));
const getContextList = () => {
  axios
    .get(`${BASE_URL}/faq/`, {
      params: {
        userRole: user.userRole,
      },
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    })
    .then(data => {
      // console.log(data.data.content);
      // boardList.value = data.data.content;
      console.log('data: ', data.data.content);
      boardList.value = data.data.content;
    })
    .catch(err => {
      console.log(err);
    });
};
</script>

<style lang="scss" scoped></style>
