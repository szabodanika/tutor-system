export default class Service {
  http = null
  baseUrl = null
  serviceUrl = ''
  headers = {
    "Access-Control-Allow-Origin": "http://localhost:3000",
    'Access-Control-Allow-Credentials': 'true'
  }

  constructor(axios, baseUrl) {
    this.http = axios
    this.baseUrl = baseUrl
  }

  async activateStudent(activationcode, email, password) {
    const {data} = await this.http.get(`${this.baseUrl}/activatestudent`, {
      params: {
        activationcode,
        email,
        password
      }, withCredentials: true
    })
    return data
  }

  async register(email, password, firstname, lastname, tutor, rate) {
    const {data} = await this.http.get(`${this.baseUrl}/register`, {
      params: {
        firstname,
        lastname,
        password,
        email,
        tutor,
        rate
      }, withCredentials: true
    })
    return data
  }

  async editStudentAccount(firstname, lastname, email, phone, password) {
    const {data} = await this.http.get(`${this.baseUrl}/editstudentaccount`, {
      params: {
        firstname, lastname, email, phone, password
      }, withCredentials: true
    })
    return data
  }

  async editTutorAccount(firstname, lastname, email, phone, rate, info, password) {
    const {data} = await this.http.get(`${this.baseUrl}/edittutoraccount`, {
      params: {
        firstname, lastname, email, phone, rate, info, password
      }, withCredentials: true
    })
    return data
  }

  async deactivate() {
    const {data} = await this.http.get(`${this.baseUrl}/deactivate`, {withCredentials: true})
    return data
  }

  async registerVirtualStudent(firstname, lastname, rate) {
    const {data} = await this.http.get(`${this.baseUrl}/registervirtualstudent`, {
      params: {
        firstname,
        lastname,
        rate
      }, withCredentials: true
    })
    return data
  }

  async login(email, password) {
    const {data} = await this.http.get(`${this.baseUrl}/login`, {params: {email, password}, withCredentials: true})
    return data
  }

  async signOut() {
    const {data} = await this.http.get(`${this.baseUrl}/signout`, {withCredentials: true})
    return data
  }

  async saveLesson(id, student, start, end, date, location) {
    const {data} = await this.http.get(`${this.baseUrl}/savelesson`, {
      params: {
        id: id,
        student: student,
        start,
        end,
        date,
        location,
      }, withCredentials: true
    })
    return data
  }

  async getLessonById(id) {
    const {data} = await this.http.get(`${this.baseUrl}/getlesson`, {params: {id}, withCredentials: true})
    data.start = new Date(data.start)
    data.end = new Date(data.end)
    return data
  }

  async lockLesson(id) {
    const {data} = await this.http.get(`${this.baseUrl}/locklesson`, {params: {id}, withCredentials: true})
    return data
  }

  async unlockLesson(id) {
    const {data} = await this.http.get(`${this.baseUrl}/unlocklesson`, {params: {id}, withCredentials: true})
    return data
  }

  async updatePayment(id, tutor, student, amount, cash, transaction) {
    const {data} = await this.http.get(`${this.baseUrl}/savepayment`, {
      params: {
        id: id == -1 ? null : id,
        tutor,
        student,
        amount,
        cash,
        transaction,
      }, withCredentials: true
    },)
    return data
  }

  async savePayment(id, tutor, student, amount, cash, transaction) {
    return this.updatePayment(-1, tutor, student, amount, cash, transaction)
  }

  async resetTutorCode(tutor) {
    const {data} = await this.http.get(`${this.baseUrl}/resettutorcode`, {params: {tutor}, withCredentials: true})
    return data
  }

  async getUserById(id) {
    const {data} = await this.http.get(`${this.baseUrl}/user`, {params: {id}, withCredentials: true})
    data.registered = new Date(data.registered)
    return data
  }

  async saveStudent(id, rate) {
    const {data} = await this.http.get(`${this.baseUrl}/savestudent`, {
      params: {
        id,
        rate
      }, withCredentials: true
    })
    return data
  }

  async disableStudent(id) {
    const {data} = await this.http.get(`${this.baseUrl}/disablestudent`, {
      params: {id}, withCredentials: true
    })
    return data
  }


  async getPaymentsSent(user) {
    const {data} = await this.http.get(`${this.baseUrl}/paymentssent`, {params: {user}, withCredentials: true})
    return data.map(payment => ({
      ...payment,
      date: new Date(payment.date)
    }))
  }

  async getPaymentsReceived(user) {
    const {data} = await this.http.get(`${this.baseUrl}/paymentsreceived`, {params: {user}, withCredentials: true})
    return data.map(payment => ({
      ...payment,
      date: new Date(payment.date)
    }))
  }

  async getStudents(tutor) {
    const {data} = await this.http.get(`${this.baseUrl}/students`, {params: {tutor}, withCredentials: true})
    return data.map(student => ({
      ...student,
      registered: new Date(student.registered)
    }))
  }

  async getLessonByTutor(tutor, week) {
    const {data} = await this.http.get(`${this.baseUrl}/tutorlessons`, {params: {tutor, week}, withCredentials: true})
    return data.map(lesson => ({
      ...lesson,
      start: new Date(lesson.start),
      end: new Date(lesson.end),
      tutor: {
        ...lesson.tutor,
        registered: new Date(lesson.tutor.registered)
      },
      student: {
        ...lesson.student,
        registered: new Date(lesson.student.registered)
      },
    }))
  }

  async getLessonByStudent(student, week) {
    const {data} = await this.http.get(`${this.baseUrl}/studentlessons`, {
      params: {student, week},
      withCredentials: true
    })
    return data.map(lesson => ({
      ...lesson,
      start: new Date(lesson.start),
      end: new Date(lesson.end),
      tutor: {
        ...lesson.tutor,
        registered: new Date(lesson.tutor.registered)
      },
      student: {
        ...lesson.student,
        registered: new Date(lesson.student.registered)
      },
    }))
  }
}
