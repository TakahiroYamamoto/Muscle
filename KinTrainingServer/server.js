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

function getIPAddress() {
  var interfaces = require('os').networkInterfaces();
  for (var devName in interfaces) {
    var iface = interfaces[devName];

    for (var i = 0; i < iface.length; i++) {
      var alias = iface[i];
      if (alias.family === 'IPv4' && alias.address !== '127.0.0.1' && !alias.internal)
        return alias.address;
    }
  }

  return '0.0.0.0';
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
        case 'background.jpg':
            fs.readFile('background.jpg', function(err, data) {
                if (err) throw err;
                res.writeHead(200, { 'Content-Type': 'image/jpeg'});
                res.write(data);
                res.end();
            });
            break;
        case 'win1.html':
        case 'win2.html':
            fs.readFile(path, function(err, data) {
                if (err) throw err;
                res.writeHead(200, { 'Content-Type': 'text/html'});
                res.write(data);
                res.end();
            });
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

var ip = getIPAddress();
console.log("local IP address: " + ip);
server.listen(1000, ip);
console.log("server listening...");
