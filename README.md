# Ice Dragon

Ice Dragon is a project that has been created at the `#SBHack19` hackathon.

This README will also be submitted as part of the final solution/submission.

# Required submission information

- URL to working application: [ice-dragon.ose3.puzzle.ch](https://ice-dragon.ose3.puzzle.ch)
- URL to public GitHub repository: [github.com/puzzle/ice-dragon](https://github.com/puzzle/ice-dragon)
- URL to the demo content provider page: [ice-dragon-content-demo.ose3.puzzle.ch](https://ice-dragon-content-demo.ose3.puzzle.ch)
- Short description: see next chapter

# What is Ice Dragon?

Ice Dragon is a platform that has the goal of helping both the end user and the producers of content to get a much more pleasant experience when using the web. Its two main goals are:

- Incentivise content creation by directly facilitating payments between end users and content providers
- Avoid inefficiencies of the current financial system by allowing micro payments through the Bitcoin Lightning Network

## For end users

Do you sometimes feel like you, as a user, have become the product of the web as it currently works? You have to consume massive amounts of advertisement (or get harassed for using an Ad Blocker) and give away your personal information for free almost everywhere you go?

Ice Dragon is a platform that aims to incentivise end users to pay small amounts of money for the content they consume. As a paying customer you can then expect a streamlined and advertisement free experience. Furthermore, we believe that you should be able to pay for a content subscription without revealing any personal information about you or your credit card.  
The Ice Dragon platform lists compatible content providers and allows you to pay for their services easily with Bitcoin over the Lightning Network.

### Why you should use Ice Dragon to pay for content:

- You can register completely pseudonymous by using your Lightning Network node (the node's public is your identity, signing a message wit the node is your password)
- You can see a list of all content providers and easily pay for any duration you want to use their service for
- After you pay for a provider's content, you can visit their website and use everything without seeing any advertisement or hitting an paywalls
- There is no need for the content provider to identify you or ask for personal information, you just give them the proof-of-payment in form of a browser cookie
- You can choose how much you pay by choosing the duration of your subscription in hours

## For content providers

As the creator of valuable content that users want to consume, you should be paid a fair price for your work. But it's hard to get paid for a page visit or an article alone because such small value transfers are very expensive when using Credit Cards for example. The Credit Card company ends up getting more money than you do for a payment.  
With the Lightning Network direct micro payments with no intermediaries are possible. So you can get an end user to pay for as little as one hour of visiting your page and in exchange deliver her/him an advertisement and paywall free experience. The paid amount goes directly to you and is settled immediately.

### Why you should use Ice Dragon to get paid for your content:

- It's very easy to set up to work with your blog or news website. You just register your website with Ice Dragon and install two very simple scripts.
  See [this README](content-provider-scripts/README.md) for more information.
- If you have your own Lightning Network node, payments will go directly to your node
- If you don't have your own node yet, we will hold on to your balance until you are ready to withdraw it to your wallet
- You can set the price for an one hour of advertisement free and paywall free visit to your website. We multiply this price by however many hours the user wants to pay for.
- You don't need to set up any account/subscription database for your users but can get paid immediately
- You respect the privacy of your users because you don't need to identify them
- You will get paid in Bitcoin, the most sound money on earth

# Technical information

The project structure has been generated with JHipster.  
All the code that is in the first commit of this repository has been automatically created and is not part of the
hackathon submission but should be considered as part of a library.

For more details, see [README-jhipster.md](README-jhipster.md).

## Development setup

### Persistent development DB

For the default icedragon-db:

```bash
docker run \
  -d \
  -e POSTGRES_USER=icedragon \
  -e POSTGRES_PASSWORD=icedragon \
  -p 5442:5432 \
  --name icedragon-db \
  --restart unless-stopped \
  postgres:11.2
```

### Launch the demo page locally

To launch the content demo container, go to the directory `content-provider-demo-pages` and launch the following command:

```bash
docker-compose up
```

# License / attribution

This project is licensed under the [MIT](LICENSE) license.

The logo is based on [this image](https://www.pngarts.com/explore/119269) licensed under CC 4.0 BY-NC.
