# Dropbox_API_Usage
Initial Commit, Working copy of oauth Implementation of Dropbox API

Important 

src/main/resources has dropboxConfig.properties which has

dropbox.APP_KEY=

dropbox.APP_SECRET=

dropbox.REDIRECT_URI=

dropbox.ENCODE_VALUE=

Need to fill in these values in there so that the values will be used in the application for authentication to use dropbox api. 

Note: 
- Without these values the application would not run.
- You would get APP_KEY, APP_SECRET values in here "https://www.dropbox.com/developers/apps" by creating a new app.
- After you create an app details, there would be an app settings where-in you can add a REDIRECT_URI OAuth2 Redirect URI's section.

How to run, 
After you check out the project, 
you can create a new maven build project and type in "clean install jetty:run" as goals in eclipse
