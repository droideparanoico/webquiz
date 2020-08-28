# WebQuizEngine
Spring Boot web service to create and solve quizzes using a REST API.
## Register user
To register a new user send a JSON with `email` and `password` via `POST` request to `/api/register`:
``` json
{
"email": "test@gmail.com",
"password": "secret"
}
```
If the email is already taken by another user, the service will return the 400 (Bad request) status code.
The email also must have a valid format (with @ and .) and the password must have at least five characters.
## Create quiz
In order to create a quiz send a JSON via `POST` request to `api/quizzes` with this format:
``` json
{
"title": "The Ultimate Question",
"text": "What is the answer to the Ultimate Question of Life, the Universe and Everything?",
"options": ["Everything goes right","42","2+2=4","11011100"],
"answer": [1]
}
```
The answer equals [1] corresponds to the second item from the options array (multiple choice questions are supported)

If the number of options in the quiz is less than 2 the server responds with `400 (Bad request)` status code.

If everything is correct, the server response is a JSON with four fields: id, title, text and options:
```json
{
  "id": 1,
  "title": "The Ultimate Question",
  "text": "What is the answer to the Ultimate Question of Life, the Universe and Everything?",
  "options": ["Everything goes right","42","2+2=4","11011100"]
}
```
## Get quizzes
Send a `GET` request to `api/quizzes/{id}` to get quiz by its id.

Send a `GET` request to `api/quizzes` to get all quizzes.

Response contains a JSON with quizzes (inside content) and some additional metadata, i.e:

```json
{
  "totalPages":1,
  "totalElements":3,
  "last":true,
  "first":true,
  "sort":{ },
  "number":0,
  "numberOfElements":3,
  "size":10,
  "empty":false,
  "pageable": { },
  "content":[
    {"id":102,"title":"Test 1","text":"Text 1","options":["a","b","c"]},
    {"id":103,"title":"Test 2","text":"Text 2","options":["a", "b", "c", "d"]},
    {"id":202,"title":"The Java Logo","text":"What is depicted on the Java logo?",
     "options":["Robot","Tea leaf","Cup of coffee","Bug"]}
  ]
}
```

The API support the navigation through pages by passing the page parameter: `/api/quizzes?page=1`
## Solve quiz
For solving quiz send a JSON via `POST` request to `api/quizzes/{id}/solve`:

``` json
{
"answer": [2]
}
```
Where answer is the indexes of the correct answers, and id is the question id.

It is also possible to send an empty array [] since some quizzes may not have correct options.

The service returns a JSON with two fields: `success` (`true` or `false`) and `feedback` (just a string).

There are three possible responses:

- If the passed answer is correct:
`{"success":true,"feedback":"Congratulations, you're right!"}`
- If the answer is incorrect:
`{"success":false,"feedback":"Wrong answer! Please, try again."}`
- If the specified quiz does not exist, the server returns the `404 (Not found)` status code.

## Get completed quizzes
Send a `GET` request to `/api/quizzes/completed` together with the user auth data to get completed quizzes by user.

It is allowed to solve a quiz multiple times and completions are sorted from the most recent to the oldest.

Response example:

```json
{
  "totalPages":1,
  "totalElements":5,
  "last":true,
  "first":true,
  "empty":false,
  "content":[
    {"id":103,"completedAt":"2019-10-29T21:13:53.779542"},
    {"id":102,"completedAt":"2019-10-29T21:13:52.324993"},
    {"id":101,"completedAt":"2019-10-29T18:59:58.387267"},
    {"id":101,"completedAt":"2019-10-29T18:59:55.303268"},
    {"id":202,"completedAt":"2019-10-29T18:59:54.033801"}
  ]
}
```
## Delete quiz
A user can delete their quiz by sending the `DELETE` request to `/api/quizzes/{id}`

If the operation was successful, the service returns the `204 (No content)` status code without any content.

If the specified quiz does not exist, the server returns `404 (Not found)`. If the specified user is not the author of this quiz, the response is the `403 (Forbidden)` status code.