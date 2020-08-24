(ns kodemaker-no.ingestion.video-test
  (:require [kodemaker-no.ingestion.video :as sut]
            [midje.sweet :as midje]))

(midje/fact
 "It recognizes urls"
 (sut/find-video "http://www.youtube.com/watch?v=y5PSRn56ZWo") => {:type :youtube, :id "y5PSRn56ZWo"}
 (sut/find-video "https://www.youtube.com/watch?v=y5PSRn56ZWo") => {:type :youtube, :id "y5PSRn56ZWo"}
 (sut/find-video "https://youtu.be/Nhhm5yC2HCo") => {:type :youtube, :id "Nhhm5yC2HCo"}
 (sut/find-video "http://vimeo.com/49324971") => {:type :vimeo, :id "49324971"}
 (sut/find-video "https://vimeo.com/28765670") => {:type :vimeo, :id "28765670"}
 (sut/find-video "http://vimeo.com/album/2525252/video/74401304") => {:type :vimeo, :id "74401304"}
 (sut/find-video "http://vimeo.com/user18356272/review/96634125/3419ad5e0a") => {:type :vimeo, :id "96634125"}
 (sut/find-video "https://vimeo.com/album/3556815/video/138956041") => {:type :vimeo, :id "138956041"}
 (sut/find-video "https://vimeo.com/showcase/6264904/video/360782295") => {:type :vimeo, :id "360782295"}
 )
