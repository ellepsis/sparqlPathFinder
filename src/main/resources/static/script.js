"use strict";

$("#submitSearch").click(() => {
    let sourceVal = $("#sourceInput").val();
    let targetVal = $("#targetInput").val();
    $.ajax('/api/request/search', {
        data: JSON.stringify({source: sourceVal, target: targetVal}),
        contentType: 'application/json',
        type: 'POST',
        success: () => alert("Success")
    });
});