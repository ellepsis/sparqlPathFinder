"use strict";

$(document).ready(function () {
    $("#submitSearch").click(() => {
        let sourceVal = $("#sourceInput").val();
        let targetVal = $("#targetInput").val();
        let maxDepth = $("#maxDepth").val();
        $.ajax('/api/request/search', {
            data: JSON.stringify({source: sourceVal, target: targetVal, maxDepth: maxDepth}),
            contentType: 'application/json',
            type: 'POST',
            url: 'api/request/search',
            success: (response) => {
                displayResult(response);
            },
            error: (response) => {
                if (response.responseJSON != null) {
                    if (response.responseJSON.message != null) {
                        alert(response.responseJSON.message)
                    } else {
                        alert(response.responseText);
                    }
                } else {
                    alert("An error have been occurred. See server logs.")
                }
            }
        });
    });
});


function displayResult(res) {
    var resString = "";
    if (res == null || res === "") {
        resString = "Path is not found";
    } else {
        resString += nodeResultToString(res);
    }
    $("#textResult").html(resString);
}

function nodeResultToString(node) {
    var result = "";
    if (node == null) {
        return result;
    }
    if (node.parentNode != null) {
        result += nodeResultToString(node.parentNode) + "<br/>|<br/>|<br/>↓";
    }
    result += "<br/>  depth: " + node.depth;
    result += "<br/> " + parentResultToString(node.parentResult);
    result += "<br/> model: " + node.model;
    return result;
}

function parentResultToString(executionResult) {
    return executionResult.subject + " -> " + executionResult.relation + " -> " + executionResult.target;

}