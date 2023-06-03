# words-of-wonders-solver

### This is a project I built for experimenting with Spring Data JPA Criteria API and custom repositories for PostreSQL.

Given a set of letters, this application will return all the valid words that can be formed by using those letters.

The application exposes two endpoints:

## POST /api/v1/load

This endpoint loads the valid words to the PostgreSQL database. This endpoint should be called once when starting the application for the first time.

## GET /api/v1/{input}

This endpoint will return all the valid words that can be formed using the letters in the input string. It also takes optional query parameters:

| Query Parameter | Value | Description |
| -------- | -------- | -------- |
| page    | Integer | Pagination parameter, responses will start from the provided page. Defaults to 0. |
| size    | Integer | Pagination parameter, provided number of responses will be returned. If not provided returns all the possible words. |
| sort    | String  | Sorting parameter, can be either "word" to sort in alphabetical order (this is the default), or "length" to sort by length. ",desc" can be appended at the end of the value to reverse sorting criteria |
| min     | Integer | Minimum length of the words in the response. Defaults to 3. |
| max     | Integer | Maximum length of the words in the response. Defaults to the length of the input letters. |

### Example usages:

* GET http://localhost:8080/api/v1/dirnvgi
* GET http://localhost:8080/api/v1/dirnvgi?page=1&size=10&sort=length,desc&min=3&max=7

## Running the application

One can clone the repo, open it on IntelliJ and it will be ready to run.
