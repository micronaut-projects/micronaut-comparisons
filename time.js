const spawn = require("child_process").spawn;
const http = require("http");

const pingIntervalMs = 10;

const args = process.argv.slice(2);
if (args.length == 0) {
  console.log('The path of java executable jar must be specified !');
}

const proc = spawn("java", ["-Xmx128m", "-jar", args[0]]);

const startTime = new Date().getTime();
const intervalHandle = setInterval(async () => {
  const {error, response, body} = await request("http://localhost:8080/hello-world");

  if (!error && response && response.statusCode === 200 && body) {
    const time = new Date().getTime() - startTime;
    console.log(time + " ms");
    clearInterval(intervalHandle);
    proc.kill();
    process.exit(0);
  } else {
    if (!error || !error.code === "ECONNREFUSED") {
      console.log({error, response, body});
      console.log(error ? error : response.statusCode);
    }
  }
}, pingIntervalMs);

function request(url, cb) {
  return new Promise((resolve, reject) => {
    const request = http.request(url, response => {

      let body = "";

      response.on('data', data => {
        body += data;
      });

      response.on('end', () => {
        resolve({
          error: null,
          response,
          body
        });
      });
    });

    request.on('error', error => {
      resolve({
        error,
        response: null,
        body: null
      });
    });

    request.end();
  });
}
