<template>
  <div class='container'>
    <b-row>
      <b-col>
        <b-card>
          <template #header>
            <br>
            <h1 align='middle' class='text-primary'>
              Welcome to oktatutor
            </h1>
            <p align='middle' class='text-muted'>
              Beta v1.1 made with love by Dániel Szabó
            </p>
          </template>
          <b-card-body>
            <div align='middle'>
              <b-button pill size='lg' variant='outline-primary' @click='$nuxt.$router.push("/login")'>Sign
                in
                <b-icon icon='arrow-right'></b-icon>
              </b-button>
            </div>
            <br>
          </b-card-body>
        </b-card>
        <br>
        <br>
        <b-card>
          <b-card-body>
            <b-row>
              <b-col xl='6' md='6' sm='12'>
                <h3>Students</h3>
              </b-col>
              <b-col xl='6' md='6' sm='12'>
                <h3>Tutors</h3>
              </b-col>
            </b-row>
          </b-card-body>
        </b-card>
        <br>
        <br>
        <b-card>
          <b-card-body>
            <h3>Contribution</h3>
          </b-card-body>
        </b-card>
        <br>
        <br>
        <b-card>
          <b-card-body>
            <h3>Support</h3>
          </b-card-body>
        </b-card>
      </b-col>
    </b-row>
  </div>
</template>

<script>
export default {
  name: "Welcome",
  layout: "Index"
}
</script>

<style scoped>

</style>