---
title: Effective Integration Testing
verticalSeparator: -v-
theme: white
revealOptions:
  transition: 'fade'

---
### Effective Integration Testing

Andrew Rembrandt<br/>

<small>@andrewrembrandt.bsky.social<br/>
https://rembrandt.dev/contact</small>

<div id="bottom-right">
SocratesCH '24<br/>
</div>

---
### Why & What

* We've nailed effective unit testing
    * IT not understood, effective (or implemented 😞)
* ! End-to-End (E2E) tests
    * 1 app/service under test, with
        * 2nd & 3rd party components typically running in docker[-compose]

---
### Why & What
* Benefits are often more significant that unit tests
* Immediate feedback when these are broken 🥳
    * SQL statements or ORM mapper (e.g. Hibernate entities)
    * HTTP parameters, [de]serialization, TCP/security setup
    * PubSub/Message-bus setup or messages
    * Component layer integration within the app/service

---
### What
* [Spring-]Slices or complete app/service?
    * Optimise for performance, e.g
        * @DataJpaTest, @WebMvcTest, & @Import / @TestConfiguration, common base class
    * Use core framework support
* Build tool support
    * Maven failsafe & <u>skipITs</u> parameter

---
### Why & What
* Use 3rd party libraries & tools
    * TestContainers/docker-compose for internal & 3rd party external services (DB, pubsub, etc)
    * Bash scripts

---
### Common Misconceptions / Myths
* ITs aren't for frontend
* ITs are too slow to run in CI or Pull/Merge Requests
    * Can easily be made to run fast
        * Parallel
        * tmpfs for 3rd party containers
        * test slices
* ITs aren't 'pure' like unit tests

---
### Essentials
* Like unit tests, ITs _must always_ run in CI
    * PR/MRs will break them & neglect them if not
* At least one IT in every project that devs can copy/build upon
* Must be in the same codebase as the app/service code
    * Ideally alongside unit tests (=> Reuse test utils)
* Written in the same language as the app/service code

---
### People complain, it's too hard
* Less is more
* Easier than unit tests (!mocking)
* ChatGPT / AI code generation
* Functional coverage with less

---
### Containers and Tools
* Bash ~shell~ scripts
    * Loop till services have started
* Multiple java processes
    * Can easily debug 2nd party services
* nginx reverse proxy
    * Useful for frontend code having backend services available at the correct urls / paths

---
### Optimisation / Speed
* Critical to run fast
* DB / 3rd party tools
    * Persist state with DB backups, or docker volumes
* Run in parallel
    * Ensure idempotency or non conflicting data between tests
        * Insert unique users / grouping elements for each test

---
### Example code 
* Java (WIP)<br/>
<small>https://github.com/andrewrembrandt/effective-integration-testing-talk/</small>
* Go<br/>
<small>TBP</small>

---
### Questions
