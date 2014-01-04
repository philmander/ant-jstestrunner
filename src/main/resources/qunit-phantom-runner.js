if (phantom.args.length === 0 || phantom.args.length > 2) {
    console.log('Usage: qunit-phantom-runner.js URL');
    phantom.exit();
}

var url = phantom.args[0];

var page = new WebPage();

page.onAlert = function(msg) {
	console.log(msg);
};

page.onError = function (msg, trace) {
    console.log("Test error: " + msg);
    
    trace.forEach(function(item) {
        console.log('  ', item.file, ':', item.line);
    });
    
    exit(1);
};

page.onInitialized = function() {
	page.evaluate(function() {
		window.document.addEventListener( "DOMContentLoaded", function() {
			
			//these prefixes are read by the ant task, update the phantom runner if changing them
			var PASS_MESSAGE = "Test passed.";
			var FAIL_MESSAGE = "Test failed.";
						
			var testCount = 0;
			var assertionCount = 0;
			var messages = [];
			var currentModule = "";
			
			QUnit.moduleStart(function(module) {				
				currentModule = module.name + ": ";
			});
			
			QUnit.moduleDone(function(result) {
				currentModule = "";
			});
			
			QUnit.testDone(function(result) {
				
				assertionCount = 0;
				testCount++;
													
				alert(testCount + ". " + currentModule + result.name + " (" + result.passed + ", " + result.failed + ", " + result.total + ")");		
				
				messages.forEach(function(message) {
					alert(message);
				});
				messages = [];

				var status = result.failed == 0 ? PASS_MESSAGE : FAIL_MESSAGE;
				alert(status);
			});
			
			QUnit.log = function(test) {
				
				assertionCount++;
						
				var indent = "    ";
				var message = test.message || "";
		    	if(test.result) {
		    		messages.push(indent + assertionCount + ". Pass: " + message);
		    	} 
				else {
					messages.push(indent + assertionCount + ". Fail: " + message);
					messages.push(indent + "Expected [" + test.expected + "] but was [" + test.actual + "].");
		    	}
			};
				
			//failed, passed, total
			QUnit.done(function(result){
				alert("Tests completed in " + result.runtime + " milliseconds");
				alert(result.passed + " tests of " + result.total + " passed, " + result.failed + " failed.")
				window.qunitDone = true;
			});			

		}, false );	
	});
};

console.log("\nRunning " + url + "...");
page.open(url, function(status){
	
	if (status !== "success") {
		console.log("Unable to access network: " + status);
        exit(1);
	} else {
		var interval = setInterval(function() {
			
			
			if (finished()) {
				clearInterval(interval);
                exit(0);
			}
		}, 100);
	}
});

function finished() {
	return page.evaluate(function(){
		return window.qunitDone;
  });
};

// Hack for phantom.exit not always working.
//
// PhantomTestRunner (see line 144) is looking for "EOF" while reading the process output stream
// and will break the processing loop (and eventually destroy the phantomjs.exe process) if EOF is detected.
//
// See
//      https://groups.google.com/forum/?fromgroups#!topic/phantomjs/kaEzJ45VS0c
//
// for a lively discussion on phantomjs not always exiting properly in windows.
function exit(errorCode) {
    console.log("Exiting with return code: " + errorCode);
    console.log("EOF");
    phantom.exit(errorCode);
};