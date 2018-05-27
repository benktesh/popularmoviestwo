











TODO Lists:
- Use styles in layout
- Add unit tests


Sources for Stage 2:
Passing multipel parameters to async tasks
https://stackoverflow.com/questions/12069669/how-can-you-pass-multiple-primitive-parameters-to-asynctask

Get thumbnail image of youtube
https://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api







The following notes from stage 1 are here for reference purpose.
Notes:
- Please replace api key in apiData.xml for key "api_key"
- Screens.gif shows some shots of the app on emulator


Review comments:
- The reviews and suggestion were helpful. I am going to take that class in kotlin
- Set the connection time out to 10 seconds and read time out to 20 seconds
- Make network connection via async task
- Used databinding for detailed activity
- Handled app crashing while starting up wwhen there was no-network event caused by empty item list.
- Added some design time tools (text and itemcount)
- Correctly named this file to 'ReadMe' from 'ReamMe' :)

Sources/References used during this project:
- I used some logic from Grow With Google recycler view lessons i.e. GreenAdapter.java that I had previously worked.
- Code about network connection check was abstracted from stackoverflow post's answers. https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
- Used https://developer.android.com/reference/android/widget/RatingBar and https://stackoverflow.com/questions/46383927/android-ratingbar-small-style?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa for rating bar
- Used https://stackoverflow.com/questions/28998327/how-to-add-a-simple-8dp-header-footer-to-androids-recyclerview?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa to get some idea to handle padding on recycler view
- Used https://inthecheesefactory.com/blog/understand-android-activity-launchmode/en to understand the launch mode and single task to presist the view
- I encountered threading issue and post https://stackoverflow.com/questions/11608173/strictmodebuilder-permitall-not-working?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa helped me resolve this issue.