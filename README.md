# GitRepoAPILookup
API to pull data about specific user's GitHub repos

## Table of Contents

- [Description](#description)
- [Installation](#installation)
- [Usage](#usage)
- [Settings](#settings)
- [License](#license)

## Description
This is a simple API that allows you to pull data about a specific user's GitHub repositories. It uses the [GitHub API](https://docs.github.com/en/rest) to pull the data and then returns it in a JSON format.


## Installation

This API uses Java 21. To run the server, you need to have it installed on your machine. You can download Java from the [official website](https://www.oracle.com/java/technologies/downloads/#java21).
To start the server, open terminal in base directory, by default `GitRepoAPILookup` and  run the following command:

#### For Windows:
```gradlew bootrun```

#### For Linux:
```./gradlew bootrun```

## Usage

To use the API, you can make a GET request to the `/api/{userName}` endpoint. If You run the server on Your local machine, the URL will look like this:

```http://localhost:8080/api/{username}```

And the API will return a JSON object with the following structure:

```json
[
  {
    "repositoryName": "{repository name}",
    "branches": [
      {
        "name": "{branch name}",
        "sha": "{last commit sha}"
      }
    ],
    "ownerLogin": "{login of owner}"
  }
]
```

and in case of username that does not exist, the API will return a JSON object with the following structure:

```json
{
  "status": "{response status code}",
  "message": "{response message}"
}
```

## Settings

All core settings are located in the `application.properties` file. You can change the server port, the GitHub API URL, and the GitHub Personal Token there.

Adding a GitHub Personal Token is not necessary, but it will increase the number of requests you can make to the GitHub API.

## License

[MIT](LICENSE) © Maciej Pietrzak

