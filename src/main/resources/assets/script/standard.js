$(function () {
    var options = $("#options");
    var log = $("#log");
    var form = $("#form");
    var cypher = $("#cypher");
    var searchfield = $("#searchfield");
    var selected = -1;

    function endsWith(str, suffix) {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
    }

    function endsWithOneOf(str, arr) {
        var returnCode = false;
        $.each(arr, function (index, value) {
            if (endsWith(str, value + " ")) {
                returnCode = true;
            }
        });
        return returnCode;
    }

    function adjustFontSize(query) {
        if (query.length > 70) {
            searchfield.css("font-size", "20px");
        } else if (query.length > 50) {
            searchfield.css("font-size", "30px");
        } else if (query.length > 40) {
            searchfield.css("font-size", "40px");
        } else {
            searchfield.css("font-size", "50px");
        }
    }

    function clearAndHide(element) {
        element.empty();
        element.hide();
    }

    var funk = function (e) {
        var keyCode = e.keyCode || e.which,
            arrow = {left: 37, up: 38, right: 39, down: 40 };
        var option = $("li.option");
        if (option.length > 0) {
            switch (keyCode) {
                case arrow.up:
                    selected--;
                    if (selected < 0) {
                        selected = 0;
                    }
                    $(option.get(selected + 1)).css("background-color", "#FFF");
                    $(option.get(selected)).css("background-color", "#CCC");
                    var query = $(option.get(selected)).data("text");
                    searchfield.val(query);
                    adjustFontSize(query);
                    break;
                case arrow.down:
                    selected++;
                    if (selected >= option.length) {
                        selected = option.length -1;
                    }
                    $(option.get(selected - 1)).css("background-color", "#FFF");
                    $(option.get(selected)).css("background-color", "#CCC");
                    var query = $(option.get(selected)).data("text");
                    searchfield.val(query);
                    adjustFontSize(query);
                    break;
            }
        }
    };

    form.submit(function () {
        $.ajax({
            type: "GET",
            url: '/rest/database/sok',
            data: 'term=' + $("#searchfield").val(),
            success: function (msg) {
                var html = "<ul>";
                $.each(msg.navn, function (index, value) {
                    html += "<li>" + value + "</li>"
                });
                html += "</ul>"
                log.html(html);
                log.show();
                cypher.html(msg.cypher);
                selected = -1;
            }
        });
        return false;
    });

    var clickOption = function () {
        var query = $(this).data("text");
        searchfield.val(query);
        adjustFontSize(query);
        form.submit();
        log.show();
        options.hide();
    };

    searchfield.keyup(function (e) {
        var keyCode = e.keyCode || e.which,
            arrow = {left: 37, up: 38, right: 39, down: 40 };
        if (keyCode == arrow.up || keyCode == arrow.down) {
            return;
        }
        var query = $(this).val();
        adjustFontSize(query);
        if (endsWithOneOf(query, ["kan", "med", "hos", "bruker", "av"])) {
            $.ajax({
                type: "GET",
                url: '/rest/database/sok',
                data: 'term=' + $("#searchfield").val(),
                success: function (msg) {
                    log.hide();
                    options.empty();
                    options.show();
                    $.each(msg.navn, function (index, value) {
                        var text = query + '"' + value + '"';
                        options.append("<li class='option' data-text='" + text + "'>" + text + "</li>");
                        $("li.option").click(clickOption);
                        $("li.option").keydown(funk);
                    });
                    selected = -1;
                }
            });
        } else if (query == "") {
            clearAndHide($("#log"));
        } else {
            clearAndHide($("#options"));
        }
    });

    searchfield.keydown(funk);

});