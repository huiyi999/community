/**
 * reply post
 */
function reply() {
    var questionId = $("#post_id").val();
    var content = $("#comment_content").val();
    comment(questionId, 1, content);
}

/**
 * reply comment
 */
function subReply(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment(commentId, 2, content);
}

function comment(targetId, type, content) {

    if (!content) {
        alert("The reply is empty!");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            console.log(response.code);
            if (response.code == 200) {
                window.location.reload();
                // $("comment_section").hide();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("http://localhost:8887/toLoginPage")
                        // window.location.href = "/toLoginPage"
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });

    console.log(targetId);
    console.log(content);
}

/**
 *
 * like (vote)
 */
function likeComment(e) {
    var targetId = e.getAttribute("data-id");
    console.log(targetId);
    // debugger;
    $.ajax({
        type: "POST",
        url: "/likeComment",
        data: {id: targetId},
        success: function (response) {
            console.log(response.code);
            // if (response.code == 200) {
            //     window.location.reload();
            // }
            if (response.code == 200) {
                window.location.reload();
                // $("comment_section").hide();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("http://localhost:8887/toLoginPage")
                        // window.location.href = "/toLoginPage"
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });

}

/**
 * collapse sub-comment
 */
function collapseComments(e) {
    // debugger;
    console.log(e);
    var id = e.getAttribute("data-id");  // user-defined attribute
    var comments = $("#comment-" + id);

    // get status of sub-comment collapse
    var collapse = e.getAttribute("data-collapse");
    console.log(collapse)
    console.log(comments)
    if (collapse) {  // exist, then remove
        // fold sub-comment
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        // debugger;
        var subCommentContainer = $("#comment-" + id);
        console.log(subCommentContainer);

        // comments.addClass("in");
        // e.setAttribute("data-collapse", "in");
        // e.classList.add("active");

        if (subCommentContainer.children().length != 1) {
            // fold
            comments.addClass("in");
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                console.log(data);
                $.each(data.data.reverse(), function (index, comment) {
                    console.log(comment);
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.username
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);

                });
                comments.addClass("in");
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function showSelectTag() {
    $("#select-tag").show();
}

function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();

    if (previous) {
        if (previous.indexOf(value) == -1) {
            $("#tag").val(previous + ',' + value);
        }
    } else
        $("#tag").val(value);

}







