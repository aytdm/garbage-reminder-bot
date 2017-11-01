# garbage reminder bot

## What is this?

This is a LINE BOT API application.  
This application remind you the day of your pickup schedule.

## Usage
### 1. Get A LINE devloper account
https://developers.line.me/en/

### 2. Create LINE channel
Remember your user id, channel secret and channel access token.

see https://developers.line.me/en/docs/messaging-api/getting-started/

### 3. Sign up Heroku
https://www.heroku.com/

### 4. Create application and set variables
Go to dashboard
Click "Setting"
you'll find "Config Variables", add following variables:

+ LINE_BOT_CHANNEL_SECRET : your channel secret
+ LINE_BOT_CHANNEL_TOKEN : your channel access token
+ LINE_ID : your user id
+ LINE_CRON : cron (see http://www.nncron.ru/help/EN/working/cron-format.htm)

see https://devcenter.heroku.com/articles/config-vars

### 5. Go back to LINE dashboard, set a webhook URL
To enable webhooks, select Use webhooks. 
Enter a webhook URL for your bot in the "Channel settings" page on the console.  
URL: https://{YOUR_HEROKU_APP_NAME}.herokuapp.com/callback

see https://developers.line.me/en/docs/messaging-api/building-bot/

### 6. Deploying application on Heroku
see https://developers.line.me/en/docs/messaging-api/building-sample-bot-with-heroku/

### 7. Have fun ：）
![](https://raw.githubusercontent.com/aytdm/garbage-reminder/images/sample.png)
