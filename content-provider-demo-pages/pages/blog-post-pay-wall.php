<?php

require_once('dragons-nest.php');

if(!hasIceDragonCookie()) {
    include('paywallSample.html');
} else {
    include('cleanSample.html');
}

?>
