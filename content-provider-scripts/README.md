# Content Provider Scripts
### Dragon's Nest
_In the Dragon's Nest, ice dragons are gathering, waiting to be called by molested end users screaming for help.
Once called, they fly to the rescue, to tear down the (payment)walls behind which the user is trapped and to liberate him from the ubiquitous assault of advertising!_

#### Installation (for content providers)
1. Register on the Ice Dragon website
2. You'll be given a file called `dragonsNest.php`. Copy that to your web server and make it available to the world.
3. Copy the link to your `dragonsNest.php` the Ice Dragon website.
4. Remove your ads and paywall: see next chapter
5. You're all set! Lay back and #stackSats while you're working on your next masterpiece.

#### Remove Ads and Paywall for visitors with a voucher (Cookie)
Add an if-clause surrounding ad-including instruction and/or your Paywall instruction as such (code in PHP):
```php
require_once('/path/to/your/dragonNest.php');
if(!hasIceDragonCookie()) {
    // here come your ads or your paywall
}
```
That's all :-)
