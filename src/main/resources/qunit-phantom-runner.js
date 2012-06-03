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
    
    phantom.exit(1);
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
		    		messages.push(indent + assertionCount + ". " + message);
		    	} 
				else {
					messages.push(indent + assertionCount + ". " + message);
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
		phantom.exit(1);
	} else {
		var interval = setInterval(function() {
			
			
			if (finished()) {
				clearInterval(interval);
				phantom.exit();
			}
		}, 100);
	}
});

function finished() {
	return page.evaluate(function(){
		return window.qunitDone;
  });
};