if (phantom.args.length === 0 || phantom.args.length > 2) {
    exit(1);
}

var url = phantom.args[0];
var page = new WebPage();

page.onInitialized = function () {
    page.evaluate(function () {
        window.document.addEventListener("DOMContentLoaded", function () {
            var testFile = {
                tests: [],
                summary: null
            };

            var currentModule = null;
            var currentTest = null;

            QUnit.begin(function (details) {
            });

            QUnit.done(function (result) {
                testFile.summary = result;
                alert(JSON.stringify(testFile));

                window.qunitDone = true;
            });

            QUnit.moduleStart(function (module) {
                currentModule = module;
            });

            QUnit.moduleDone(function (result) {
                currentModule = null;
            });

            QUnit.testStart(function (details) {
                var test = {
                    module: currentModule,
                    assertions: []
                };

                testFile.tests.push(test);
                currentTest = test;
            });

            QUnit.testDone(function (result) {
                currentTest.result = result;
            });

            QUnit.log = function (details) {
                currentTest.assertions.push(details);
            };

        }, false);
    });
};

page.onAlert = function (msg) {
    console.log(msg);
};

page.onError = function (msg, trace) {
    var testFile = {
        error: {
            message: msg,
            trace: trace
        }
    };

    console.log(JSON.stringify(testFile));
    exit(1);
};

page.open(url, function (status) {
    if (status !== "success") {
        var testFile = {
            error: {
                message: "Unable to access network: " + status
            }
        };

        console.log(JSON.stringify(testFile));
        exit(1);
    } else {
        var interval = setInterval(function () {
            if (finished()) {
                clearInterval(interval);
                exit(0);
            }
        }, 100);
    }
});

function finished() {
    return page.evaluate(function () {
        return window.qunitDone;
    });
};

// Hack for phantom.exit not always working.
//
// PhantomTestRunner is looking for "EOF" while reading the process output stream
// and will break the processing loop (and eventually destroy the phantomjs.exe process) if EOF is detected.
//
// See https://groups.google.com/forum/?fromgroups#!topic/phantomjs/kaEzJ45VS0c
// for a lively discussion on phantomjs not always exiting properly in windows.
function exit(errorCode) {
    console.log("EOF");
    phantom.exit(errorCode);
};
