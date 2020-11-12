<template>
  <b-card id='container' v-if='payment' no-body>
    <template #header>
      <b-row no-gutters>
        <b-col>
          <div class="d-flex align-items-center">
            <b-avatar :text="payment.student.firstName.substr(0,1) + payment.student.lastName.substr(0,1)"
                      variant="primary"></b-avatar>
            <span style='margin-left: 1rem'>{{ payment.student.firstName }} {{ payment.student.lastName }} on {{formatDateTime(payment.date)}}</span>
          </div>
        </b-col>
      </b-row>
    </template>
    <b-card-body>
      <span id='amount'>
        £{{payment.amount}}
      </span>
      <br>
      Covers {{ (payment.student.rate/payment.amount).toFixed(0)}} lesson{{
        (payment.student.rate/payment.amount)>1?"s":""}}
      <br>
      {{payment.cash?"Cash payment":""}}
      {{payment.transactionNumber?`Transaction number: ${payment.transactionNumber}`:""}}
    </b-card-body>
  </b-card>
</template>

<script>
export default {
  name: "PaymentPreview",
  props: {
    payment: {}
  },
  data() {
    return {
      studentData: null
    }
  },
  created() {
    this.getStudent()
  },
  methods: {
    getStudent() {
    },
    formatDate(date) {
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
    },
    formatDateTime(date) {
      return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " " +
          date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
    }
  }
}
</script>

<style scoped>
#container {
  /*padding: 0.5rem;*/
  min-width: 200px;
  font-weight: 400;
  /*max-height: 20rem;*/
}

#amount {
  font-size: 1.4rem;
  color: green;
}
</style>