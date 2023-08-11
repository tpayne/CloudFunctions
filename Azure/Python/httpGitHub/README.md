## Python Azure Function Example

# Pre-requists
To run the functions, you will need to have installed the following

* Python 3
* Azure Core functions

# Running the sample
To run locally do the following

```console
  docker build --platform linux/amd64 . \
    -t azfuncpytest:v1 && \
  docker run --rm -t -p 3000:80 -t azfuncpytest:v1
```

Once the Docker image is running, you can invoke the function using

```console
curl http://localhost:3000/api/repo/list?username=tpayne
curl http://localhost:3000/api/version
```
