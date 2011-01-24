function loadVideoURL(videoFormId)
{
    var videoURL = $('#' + videoFormId).val();

    if(videoURL == 'undefined' || videoURL == '')
    {
        alert('No video URL entered');
    }
    else
    {
        playbackPopup(escape(videoURL));
    }
}

var pauseStatusUpdate = false;
var hasStartedPlaying = false;
var playTimer = null;

function secondsToTime(totalSeconds)
{
    var hours = Math.floor(totalSeconds / (60 * 60));
    var minBreak = totalSeconds % (60 * 60);
    var minutes = Math.floor(minBreak / 60);
    var secBreak = minBreak % 60;
    var seconds = Math.ceil(secBreak);
    return ((hours < 10) ? '0' : '') + hours + ((minutes < 10) ? ':0' : ':') + minutes + ((seconds < 10) ? ':0' : ':') + seconds;
}

function updatePlayStatus()
{
    if(!pauseStatusUpdate)
    {
        $.getJSON("/ci",
        {
            t: "4",
            d: $('#selectedpd :selected').val()
        },
        function(data)
        {
            $("#playbacktime").html(secondsToTime(data.position) + ' of ' + secondsToTime(data.duration));

            $("#playbackslider").slider( 'option', { disabled: false, max: Math.floor(data.duration), value: Math.floor(data.position) } );
        });
    }
}

function play()
{
    if(hasStartedPlaying)
    {
        $.getJSON("/ci",
        {
            t: "5",
            d: $('#selectedpd :selected').val(),
            r: '1.0'
        },
        function(data)
        {
            pauseStatusUpdate = false;
        });
    }
    else
    {
        $('#selectedpd').attr('disabled', true);

        $("#playbackslider").slider( 'option', { disabled: true, max: 0, value: 0 } );

        $.getJSON("/ci",
        {
            t: "2",
            d: $('#selectedpd :selected').val(),
            s: currentVideoURL
        },
        function(data)
        {
            hasStartedPlaying = true;
            playTimer = setInterval("updatePlayStatus()", 2000);
        });
    }
}

function pause()
{
    $.getJSON("/ci",
    {
        t: "5",
        d: $('#selectedpd :selected').val(),
        r: '0.0'
    },
    function(data)
    {
        pauseStatusUpdate = true;
    });
}

function stop()
{
    $.getJSON("/ci",
    {
        t: "3",
        d: $('#selectedpd :selected').val()
    },
    function(data)
    {
        $('#selectedpd').attr('disabled', false);
        
        $("#playbacktime").html(secondsToTime(0) + ' of ' + secondsToTime(0));

        $("#playbackslider").slider( 'option', { disabled: true, max: 0, value: 0 } );

        hasStartedPlaying = false;
        clearTimeout(playTimer);
        playTimer = null;
    });
}

var currentVideoURL;

function dialogClose(event, ui)
{
    if(playTimer != null)
    {
        stop();
    }
    currentVideoURL = null;
    hasStartedPlaying = false;

    $('#playback-window').remove();
}

function playbackPopup(videoURL)
{
    currentVideoURL = videoURL;
    hasStartedPlaying = false;
    if(playTimer != null)
    {
        clearTimeout(playTimer);
        playTimer = null;
    }

    var urlTitle = unescape(videoURL);

    $('body').prepend('<div id="playback-window"></div>');

    $('#playback-window').dialog(
    {
        width: 450,
        modal: true,
        title: urlTitle.length > 40 ? urlTitle.substring(0, 40) + '...' : urlTitle,
        resizable: false,
        close: dialogClose
    });    

    $.getJSON('/ci?t=1', function(data)
    {
        var playbackDevices = data.devices;

        var displayHTML = '';

        if (playbackDevices != null && playbackDevices.length > 0)
        {
            displayHTML += '<div id="playback_device_selection">Select a playback device: <select id="selectedpd" name="selectedpd">';
            $.each(playbackDevices, function(device)
            {
                displayHTML += '<option value="' + playbackDevices[device].deviceId + '">';
                displayHTML += playbackDevices[device].deviceName;
                displayHTML += '</option>';
            });
            displayHTML += '</select></div>';

            displayHTML += '<hr/>';

            displayHTML += 'Playback time: <span id="playbacktime">00:00:00 of 00:00:00</span><br/>';
            displayHTML += '<div id="playbackslider" style="margin:10px; height: 6px;"></div>';

            displayHTML += '</br>';
            
            displayHTML += '<div style="text-align:center">';
            displayHTML += '<span id="toolbar" class="ui-widget-header ui-corner-all" style="padding: 15px;">';
            displayHTML += '<button id="play">play</button>';
            displayHTML += '<button id="stop">stop</button>';
            displayHTML += '</span>';
            displayHTML += '</div>';
        }
        else
        {
            displayHTML += 'There are currently no devices available for playback.';
        }

        $('#playback-window').html(displayHTML);

        $('#selectedpd').attr('disabled', false);

        $('#play').button({ text: false, icons: { primary: "ui-icon-play" } }).click(function()
        {
            $('#stop').button( "option", { disabled: false } );
            var options;
            if ( $(this).text() === "play" )
            {
                play();
                options = { label: "pause", icons: { primary: "ui-icon-pause" } };
            }
            else
            {
                pause();
                options = { label: "play", icons: { primary: "ui-icon-play" } };
            }
            $(this).button( "option", options );
        });
        $('#stop').button({ text: false, disabled: true, icons: { primary: "ui-icon-stop" } }).click(function()
        {
            stop();
            $('#play').button( "option", { label: "play", icons: { primary: "ui-icon-play" } });
            $('#stop').button( "option", { disabled: true } );
        });

        $("#playbackslider").slider({
			value: 0,
			orientation: "horizontal",
			range: "min",
            max: 0,
			animate: true,
            disabled: true,
            slide: handleScrubSlide,
            stop: handleScrubSlideDone
		});

        $("#playbackslider").slider( 'option', { disabled: true, max: 0, value: 0 } );
    });
}

function handleScrubSlide(event, ui)
{
    pauseStatusUpdate = true;
    $("#playbacktime").html(secondsToTime(ui.value) + ' of ' + secondsToTime($("#playbackslider").slider('option', 'max')));
}

function handleScrubSlideDone(event, ui)
{
    $.getJSON("/ci",
    {
        t: "6",
        d: $('#selectedpd :selected').val(),
        p: ui.value
    },
    function(data)
    {
        $("#playbacktime").html(secondsToTime(ui.value) + ' of ' + secondsToTime($("#playbackslider").slider('option', 'max')));
        pauseStatusUpdate = false;
    });
}

function refreshDeviceList()
{
    $.getJSON('/ci?t=1', function(data)
    {
        if (data.devices != null && data.devices.length > 0)
        {
            var dldata = '';
            $.each(data.devices, function(device)
            {
                dldata += '<div>';
                dldata += data.devices[device].deviceName;
                dldata += '</div>';
            });
            $('#devicelist').html(dldata);
        }
        else
        {
            $('#devicelist').html('No devices found.');
        }
    });
}

$(document).ready(function()
{
    refreshDeviceList();
});