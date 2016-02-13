var http = require('http')
var fs = require('fs')
var server = http.createServer();

var counter = { 'device1': 0, 'device2': 0 };

function printCounter(device, counter, res) {
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.write('hello world from ' + device);
    res.write('counter: ' + counter);
    res.end();
}

server.on('request', function(req, res) {
    var path = req.url.replace(/\//g, "");
    switch (path) {
        case 'device1':
        case 'device2':
            counter[path] = counter[path] + 1;
            printCounter(path, counter[path], res);
            break;
        case 'json':
            res.writeHead(200, { 'Content-Type': 'text/plain'});
            res.write(JSON.stringify(counter));
            res.end();
            break;
        case 'reset':
            counter = { 'device1': 0, 'device2': 0 };
            res.writeHead(200, { 'Content-Type': 'text/plain'});
            res.write("Counter Reset");
            res.end();
            break;
        default:
            fs.readFile('display.html', 'utf-8', function(err, data) {
                res.writeHead(200, { 'Content-Type': 'text/html'});
                res.write(data);
                res.end();
            });
            break;
    }
});

server.listen(1000, '10.10.55.237');
console.log("server listening...");
