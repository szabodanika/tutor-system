<template>
  <div class='container'>
    <b-card>
      <template #header>
        <br>
        <b-row no-gutters>
          <b-col cols='12' align='middle'>
            <div align='middle'>
              <b-img src='logo-300px.png'  class='logo'></b-img>
            </div>
          </b-col>
          <b-col cols='12' align='middle'>
            <h1 align='middle nowrap' class='oktatutor'>
              oktatutor
            </h1>
          </b-col>
        </b-row>

      </template>
      <b-card-body>
        <div align='middle'>
          <b-button pill size='lg' variant='outline-primary' @click='$nuxt.$router.push("/login")'>Sign
            in
            <b-icon icon='arrow-right'></b-icon>
          </b-button>
          <br>
          <br>
          <b-button class='start-teaching' pill size='sm' variant='outline-primary'
                    @click='$nuxt.$router.push("/signup/tutor")'>
            Start teaching
          </b-button>
          <b-button class='activate-account' pill size='sm' variant='outline-primary'
                    @click='$nuxt.$router.push("/signup/activation")'>
            Activate account
          </b-button>
        </div>
        <br>
      </b-card-body>
    </b-card>
    <br>
    <b-card>
      <h4>
        About Oktatutor
      </h4>
      <p>
        Oktatutor is a system designed for private tutors who need a reliable, powerful and clean system to manage
        their lessons, students and payments. It is currently in open beta and is being continuously developed by
        Dániel Szabó.
      </p>
      <h4>
        Contact
      </h4>
      <p>
        If you need help using the application, have feedback or any other query, feel free to email me at
        <strong>daniel.szabo@oktatutor.com</strong>.
      </p>
      <p align='middle' class='text-muted'>
        Beta v1.2 made with love by Dániel Szabó
      </p>
    </b-card>
  </div>
</template>

<script>
export default {
  name: "Welcome",
  layout: "Index"
}
</script>

<style lang='scss' scoped>
@import "assets/css/light";

.container {
  max-width: 48rem;
}

.logo {
  max-height: 8rem;
}

.oktatutor {
  font-weight: 900;
  font-family: "Rubik";
  color: $primary-dark;
  font-size: 4rem;
}

@include media-breakpoint-down(md) {
  .logo {
    max-height: 8rem;
  }
  .oktatutor {
    font-size: 2.5rem;
  }
}

.start-teaching, .activate-account {
  margin: 0.5rem;
}
</style>