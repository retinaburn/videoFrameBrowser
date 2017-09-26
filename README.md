# videoFrameBrowser
Playing around with Kotlin, ffmpeg, javafx to browse video files via framegrabs
Assist in the browsing and selecting of an still image from a video file for meme generation

Not functional for anyone but me right now!

Idea is:
  1. Give it a video file
  2. It will generate and display screenshots for every 10 minutes, starting with 00:00:00 (level 1)
  3. You click on one of those images, and it generates screenshots every minute from that starting time for 10 minutes (level 2)
  4. You click on one of those images, and it generates screenshots for every second, from that starting time for 60 seconds (level 3)
  5. You click on one of those images, and it displays the milliseconds/frames

To install: mvn install

Proposal 1:
![Proposal 1](readme/proposal1.png?raw=true)
