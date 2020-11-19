<template>
  <b-overlay>
    <div class="container" align='middle'>
      <b-col>
        <b-card id='new-payment-card' align='left'>
          <template #header>
            Add new payment
          </template>
          <b-form-group label="Select student">
            <b-form-select v-model="payment.student" :options="students"></b-form-select>
          </b-form-group>

          <b-form-group label='Enter value of payment'>
            <b-input-group prepend='£'>
              <b-form-input
                  v-model="payment.amount"
                  type="number"
                  required
                  placeholder="Payment value"
              ></b-form-input>
            </b-input-group>
          </b-form-group>
          <b-form-checkbox v-model="payment.cash">Cash payment</b-form-checkbox>
          <br>
          <b-form-group label="Transaction number">
            <b-form-input
                :disabled='payment.cash'
                v-model="payment.transactionNumber"
                placeholder="Transaction number"
            ></b-form-input>
          </b-form-group>
          <b-button block @click='submit' variant="primary">Save</b-button>
        </b-card>
      </b-col>
    </div>
  </b-overlay>
</template>

<script>

export default {
  layout: 'index',
  name: 'NewPayment',
  props: [
    'user'
  ],
  data() {
    return {
      signup: {},
      students: [],
      payment: {}
    }
  },
  mounted() {
    this.user.students.forEach((student) => {
      if(!student.disabled) {
        this.students.push({
          value: student.id,
          text: `${student.firstName} ${student.lastName ? student.lastName : ""}`
        },)
      }
    })
  },
  methods: {
    submit() {
      this.$services.service.savePayment(null, this.user.id, this.payment.student, this.payment.amount,
          this.payment.cash,
          this.payment.transactionNumber).then((res) => {
        this.$nuxt.$emit("success", "successfully_saved_payment")
        this.$nuxt.$router.push("/payments")
      }).catch((error) => {
        // check if we have a string response data. these are usually custom defined on server side
        if (error.response && (typeof error.response.data === "string" || error.response.data instanceof String))
          this.$nuxt.$emit("error", error.response.data)
        // check if we have an error message
        else if (typeof error.message === "string" || error.message instanceof String) this.$nuxt.$emit("error", error.message)
        // no error message, this was unexpected
        else this.$nuxt.$emit("error", "unexpected_error")
      })
    },
  }
}
</script>

<style>
#new-payment-card {
  max-width: 25rem;
  margin-left: 0.5rem;
  margin-right: 0.5rem;
  margin-top: 1rem;
  margin-bottom: 1rem;
}
</style>
