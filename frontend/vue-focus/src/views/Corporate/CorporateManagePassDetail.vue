<template>
  <div>
    <CorporateHeader></CorporateHeader>
    <div class="flex">
      <CorporateNavbar></CorporateNavbar>
      <div class="w-screen">
        <div class="flex flex-col space-y-10">
          <nav class="flex flex-wrap justify-between p-8 text-gray-800">
            <h1 class="font-bold">{{ companyUserName }}님, 안녕하세요</h1>
            <h3 class="font-bold text-gray-500">Pass</h3>
          </nav>
          <p class="px-10 text-lg font-medium">
            <i class="text-indigo-600 bx bxs-check-circle"></i> 전형의 합격자를
            선택해주세요
          </p>
          <div class="flex flex-col">
            <div class="flex flex-col justify-center">
              <div class="inline-block w-auto py-2 space-y-4 sm:px-6 lg:px-8">
                <div class="flex flex-wrap justify-between px-5">
                  <p class="text-2xl font-medium">계정 리스트 조회</p>
                  <div class="flex justify-center space-x-2">
                    <button
                      @click="decisionNextStep()"
                      type="button"
                      class="inline-block rounded bg-indigo-600 px-6 py-2.5 text-md font-medium uppercase leading-tight text-white shadow-md transition duration-150 ease-in-out hover:bg-indigo-700 hover:shadow-lg focus:bg-indigo-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-indigo-800 active:shadow-lg"
                    >
                      전형종료 [결과확정]
                    </button>
                  </div>
                </div>
                <div class="overflow-hidden rounded-md shadow-lg">
                  <table class="min-w-full">
                    <thead class="bg-white border-b">
                      <tr>
                        <th
                          scope="col"
                          class="px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          합불여부
                        </th>
                        <th
                          scope="col"
                          class="px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          수험번호
                        </th>
                        <th
                          scope="col"
                          class="px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          이름
                        </th>
                        <th
                          scope="col"
                          class="px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          점수
                        </th>
                        <th
                          scope="col"
                          class="px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          자소서 보기
                        </th>
                        <th
                          scope="col"
                          class="px-6 py-4 text-sm font-medium text-left text-gray-900"
                        >
                          최종학력
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr
                        v-for="(applicant, index) in applicantlist"
                        :content="applicant.value"
                        :key="index"
                        class="transition duration-300 ease-in-out bg-white border-b hover:bg-gray-100"
                      >
                        <td
                          class="px-6 py-4 text-sm font-medium text-gray-900 whitespace-nowrap"
                        >
                          <input
                            :id="'checkbox' + applicant.id"
                            type="checkbox"
                            true-value="p"
                            false-value="np"
                            class="w-4 h-4 text-indigo-900 border-gray-300 rounded focus:ring-indigo-900"
                          />
                        </td>
                        <td
                          class="px-6 py-4 text-sm font-light text-gray-900 whitespace-nowrap"
                        >
                          {{ applicant.code }}
                        </td>
                        <td
                          class="px-6 py-4 text-sm font-light text-gray-900 whitespace-nowrap"
                        >
                          {{ applicant.name }}
                        </td>
                        <td
                          class="px-6 py-4 text-sm font-light text-gray-900 whitespace-nowrap"
                        >
                          {{ Object.values(applicant.scoreList)[0] }}
                        </td>
                        <td
                          class="px-6 py-4 text-sm font-light text-gray-900 whitespace-nowrap"
                        >
                          <!-- # 모달 -->
                          <div class="pt-4">
                            <button
                              class="px-4 py-2 text-white bg-indigo-900 rounded"
                              @click="showModal = true"
                            >
                              자소서 보기
                            </button>
                            <div
                              class="fixed top-0 left-0 z-50 w-full h-full bg-white"
                              v-if="showModal"
                            >
                              <div
                                class="fixed -translate-x-1/2 -translate-y-1/2 top-1/2 left-1/2 z-60"
                              >
                                <div class="bg-white p=2 relative">
                                  <button
                                    class="absolute top-0 right-0 p-0.5"
                                    @click="showModal = false"
                                  >
                                    X
                                  </button>
                                  <img
                                    @click="showModal = false"
                                    id="introduce"
                                    src="@/assets/introduce.png"
                                    alt="Modal Image"
                                  />
                                </div>
                              </div>
                            </div>
                          </div>
                          <!-- # 모달 창 끝 -->
                        </td>
                        <td
                          class="px-6 py-4 text-sm font-light text-gray-900 whitespace-nowrap"
                        >
                          {{ applicant.lastDegree }}
                        </td>
                      </tr>
                    </tbody>
                  </table>
                  <!-- <div>
                    <div
                      v-for="(applicant, index) in applicantlist"
                      :content="applicant.value"
                      :key="index"
                    >
                      <p>{{ applicant.code }} | {{ applicant.name }}</p>
                      <input type="radio" id="{{ applicant.id }}" />
                    </div>
                    <button @click="decisionNextStep()">
                      전형 종료 [결과확정]
                    </button>
                  </div> -->
                  <!-- <button @click="DecisionPassApplicantList()">출력</button> -->
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
import CorporateHeader from '@/components/CorporateHeader.vue';
import CorporateNavbar from '@/components/CorporateNavbar.vue';

import { onMounted, ref } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const BASE_URL = 'https://i8a106.p.ssafy.io/api';
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

const showModal = ref(false);

// 합불 처리 시에 받는 데이터
// {사용자 번호 Long, process번호  Long, 합불여부=문자  "p" or "np"}
const route = useRoute();
const router = useRouter();
const applicantlist = ref([]);

const decisionNextStep = () => {
  DecisionPassApplicantList();
  const user = JSON.parse(localStorage.getItem('user'));
  let endResult = JSON.stringify(passapplicantlist.value);
  console.log('passapplicantlist: ', passapplicantlist);
  axios
    .post(`${BASE_URL}/interview/decision/pass`, endResult, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
        'Content-Type': 'application/json',
      },
    })
    .then(res => {
      console.log('decision succeed: ', res.data);
      alert('합격 처리가 완료되었습니다.');
      router.push({ name: 'CorporateManagePass' });
    })
    .catch(err => {
      console.log(err.message);
    });
};

const convertPNP = id => {
  if (document.getElementById('checkbox' + id).checked) {
    return 'P';
  }
  return 'NP';
};

const passapplicantlist = ref([]);
const DecisionPassApplicantList = () => {
  passapplicantIds.value.forEach(element => {
    let pass = convertPNP(element);
    let applicantData = {
      processId: route.params.id,
      interviewResultReq: {
        applicantId: element,
        pass: pass,
      },
    };
    passapplicantlist.value.push(applicantData);
  });
};

const passapplicantIds = ref([]);
const scoreLists = ref([]);

const getPassApplicant = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  axios
    .get(`${BASE_URL}/companyusers/process/pass/${route.params.id}`, {
      headers: {
        Authorization: `Bearer ${user.accessToken}`,
      },
    })
    .then(res => {
      console.log('getPassApplicant: ', res.data);
      applicantlist.value = res.data;
      passapplicantIds.value = applicantlist.value.map(
        applicant => applicant.id,
      );
      scoreLists.value = applicantlist.value.map(
        applicant => applicant.scoreList,
      );
    });
};

onMounted(() => {
  getCompanyUserName();
  getPassApplicant();
  getCompanyUserName();
});
</script>

<style lang="scss" scoped></style>
