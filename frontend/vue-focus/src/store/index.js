import { createStore } from 'vuex';
import axios from 'axios';

const BASE_URL = 'https://i8a106.p.ssafy.io/api';

export default createStore({
  state: {
    user: null,
    currentDepartments: null,
    currentEvaluators: null,
    currentApplicants: null,
    currentApplicantProcessId: null,
    sheets: [],
    roomID: [],
    roomCodeInfo: [],
  },

  getters: {
    loggedIn(state) {
      return !!state.user;
    },
  },

  mutations: {
    SET_USER_DATA(state, userData) {
      state.user = userData;
      console.log(state.user);
      localStorage.setItem('user', JSON.stringify(userData));
      axios.defaults.headers.common[
        'Authorization'
      ] = `Bearer ${userData.accessToken}`;
      console.log(
        'axios headers: ',
        axios.defaults.headers.common['Authorization'],
      );
    },
    LOGOUT(state) {
      state.user = null;
      localStorage.removeItem('user');
      axios.defaults.headers.common['Authorization'] = null;
    },
    SAVE_DEPARTMENTS(state, departments) {
      state.currentDepartments = departments;
      console.log('state.currentDepartments: ', state.currentDepartments);
    },
    SAVE_EVALUATORS(state, evaluators) {
      state.currentEvaluators = evaluators;
      console.log('state.currentEvaluators: ', state.currentEvaluators);
    },
    SAVE_APPLICANTS(state, applicants) {
      state.currentApplicants = applicants;
      console.log('state.currentApplicants: ', state.currentApplicants);
    },
    SAVE_CURRENT_APPLICANT_PROCESS_ID(state, processId) {
      state.currentApplicantProcessId = processId;
      console.log(
        'currentApplicantProcessId: ',
        state.currentApplicantProcessId,
      );
    },
    GET_ROOMID(state, roomID) {
      state.roomID = roomID;
      console.log('state.roomID : ', state.roomID);
      console.log('roomID[0] : ', state.roomID[0].id);
    },
    GET_ROOMCODE_INFO(state, roomCodeInfo) {
      state.roomCodeInfo = roomCodeInfo;
      console.log('roomCodeInfo : ', state.roomCodeInfo);
    },
    GET_EVALUATOR_SHEET(state, sheets) {
      state.sheets = sheets;
    },
  },

  actions: {
    login({ commit }, credentials) {
      return axios
        .post(`${BASE_URL}/auth/login`, credentials)
        .then(({ data }) => {
          commit('SET_USER_DATA', data);
          console.log('user: ', this.state.user);
        })
        .catch(err => {
          console.log('login request failed! ', err.message);
        });
    },
    logout({ commit }) {
      commit('LOGOUT');
    },
    saveDepartments({ commit }, departments) {
      commit('SAVE_DEPARTMENTS', departments);
    },
    saveEvaluators({ commit }, evaluators) {
      commit('SAVE_EVALUATORS', evaluators);
    },
    saveApplicants({ commit }, applicants) {
      commit('SAVE_APPLICANTS', applicants);
    },
    saveCurrentApplicantProcessId({ commit }, processId) {
      commit('SAVE_CURRENT_APPLICANT_PROCESS_ID', processId);
    },
    getRoomId(context) {
      const user = JSON.parse(localStorage.getItem('user'));
      axios({
        method: 'get',
        url: `${BASE_URL}/interview/schedule/1/`,
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
        },
      })
        .then(res => {
          context.commit('GET_ROOMID', res.data);
        })
        .catch(err => {
          console.log(err);
        });
    },
    getEvaluatorSheets(context) {
      const user = JSON.parse(localStorage.getItem('user'));
      axios({
        method: 'get',
        url: `${BASE_URL}/evaluation/sheets/1/`,
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
        },
      })
        .then(res => {
          console.log('평가지 잘 불러옴?', res.data);
          context.commit('GET_EVALUATOR_SHEET', res.data);
        })
        .catch(err => {
          console.log(err);
        });
    },
  },
});
