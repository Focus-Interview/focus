<template>
  <div>
    <CorporateHeader></CorporateHeader>
    <div class="flex">
      <CorporateNavbar></CorporateNavbar>

      <div class="w-screen">
        <div class="flex flex-col space-y-5">
          <nav class="flex flex-wrap justify-between p-8 text-gray-800">
            <h1 class="font-bold">{{ companyUserName }} 님, 안녕하세요</h1>
            <h3 class="font-bold text-gray-500">Process</h3>
          </nav>
          <p class="px-10 text-2xl font-medium">예정된 전형 리스트</p>
          <div class="flex justify-between mx-5">
            <p class="px-10 pt-6 text-lg font-medium">
              <i class="text-indigo-600 bx bxs-check-circle"></i> 전형을
              클릭하면 전형 세부사항을 확인하고 수정할 수 있습니다
            </p>
            <button
              @click="createProcess()"
              type="button"
              class="inline-block rounded bg-indigo-500 px-6 py-2.5 text-md font-medium uppercase leading-tight text-white shadow-md transition duration-150 ease-in-out hover:bg-indigo-700 hover:shadow-lg focus:bg-indigo-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-indigo-800 active:shadow-lg"
            >
              전형 추가
            </button>
          </div>
          <div
            v-for="interview in interviews"
            :key="interview.id"
            class="space-y-5"
          >
            <!-- 전형목록 -->
            <div
              @click.stop="processDetail(interview.id)"
              class="flex flex-row items-center justify-center my-5 space-x-10"
            >
              <div
                class="flex flex-row items-center justify-center px-24 py-4 space-x-4 text-xl font-bold text-gray-600 bg-white rounded-md shadow-md hover:bg-gray-100"
              >
                <p class="text-2xl font-black">{{ interview.name }}</p>
                <p>|</p>
                <p>
                  {{ interview.startDate.slice(0, 10) }} ~
                  {{ interview.endDate.slice(0, 10) }}
                </p>
                <p>|</p>
                <p>총 {{ interview.interviewCount }} 차</p>
              </div>

              <!-- # 버튼 그룹 -->
              <div class="flex flex-row">
                <div class="flex justify-center space-x-2">
                  <!-- <button
                  type="button"
                  class="inline-block leading-normal text-white uppercase transition duration-150 ease-in-out bg-gray-400 rounded-full shadow-lg hover:bg-gray-500 hover:shadow-lg focus:bg-gray-500 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-gray-500 active:shadow-lg w-9 h-9"
                >
                  <i class="text-xl bx bx-plus"></i>
                </button> -->
                  <!-- <button
                  type="button"
                  class="inline-block leading-normal text-white uppercase transition duration-150 ease-in-out bg-gray-400 rounded-full shadow-lg hover:bg-gray-500 hover:shadow-lg focus:bg-gray-500 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-gray-500 active:shadow-lg w-9 h-9"
                >
                  <i class="text-xl bx bx-copy"></i>
                </button> -->
                  <button
                    @click.stop="deleteProcess(interview.id)"
                    type="button"
                    class="inline-block leading-normal text-white uppercase transition duration-150 ease-in-out bg-red-400 rounded-full shadow-lg hover:bg-red-500 hover:shadow-lg focus:bg-red-500 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-red-500 active:shadow-lg w-9 h-9"
                  >
                    <i class="text-xl bx bx-x"></i>
                  </button>
                </div>
              </div>
            </div>
            <!-- <div class="flex flex-row items-center justify-center space-x-10">
            <div
              class="flex flex-row items-center justify-center px-4 py-2 space-x-4 text-gray-600 bg-transparent border-4 border-white rounded-md shadow-md opacity-80"
            >
              <p class="text-lg font-bold">전형명</p>
              <p>|</p>
              <p>시작일자 ~ 종료일자</p>
              <p>|</p>
              <p>총 N 차</p>
            </div> -->

            <!-- # 버튼 그룹 -->
            <!-- <div class="flex flex-row">
              <div class="flex justify-center pr-20">
                <button
                  type="button"
                  class="inline-block leading-normal text-white uppercase transition duration-150 ease-in-out bg-blue-400 rounded-full shadow-lg hover:bg-blue-500 hover:shadow-lg focus:bg-blue-500 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-500 active:shadow-lg w-9 h-9"
                >
                  <i class="text-xl bx bx-check"></i>
                </button>
              </div>
            </div>
          </div> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import CorporateHeader from '@/components/CorporateHeader.vue';
import CorporateNavbar from '@/components/CorporateNavbar.vue';

import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const createProcess = () => {
  router.push({ name: 'CorporateManageProcessCreate' });
};
const processDetail = processId => {
  router.push({
    name: 'CorporateManageProcessDetail',
    params: { id: processId },
  });
};

const BASE_URL = 'https://i8a106.p.ssafy.io/api';
const interviews = ref(null);

const getInterviewInfo = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  axios
    .get(`${BASE_URL}/interview/process`, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    })
    .then(res => {
      console.log('interviews: ', res.data);
      interviews.value = res.data;
    });
};

const deleteProcess = processId => {
  const user = JSON.parse(localStorage.getItem('user'));
  axios
    .delete(`${BASE_URL}/interview/process/${processId}`, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    })
    .then(res => {
      console.log('delete success!', res);
      getInterviewInfo();
    })
    .catch(err => {
      console.log(err.message);
    });
};
const companyUserName = ref('');
const getCompanyUserName = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  axios
    .get(`${BASE_URL}/companyusers/me`, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    })
    .then(res => {
      console.log(res.data);
      companyUserName.value = res.data.companyName;
    })
    .catch(err => {
      console.log(err.message);
    });
};
onMounted(() => {
  getInterviewInfo();
  getCompanyUserName();
});
</script>

<style lang="scss" scoped></style>
