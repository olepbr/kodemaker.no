:title Klippe med CSS
:author odin
:tech [:css :web]

:blurb
CSS slutter aldri å overraske. Noen ganger ramler man over noe som man i utgangspunktet ikke 
ser nytteverdien av, men som kombinert med litt kreativitet produserer et interessant resultat.

:body
CSS slutter aldri å overraske. Noen ganger ramler man over noe som man i utgangspunktet ikke 
ser nytteverdien av, men som kombinert med litt kreativitet produserer et interessant resultat.

## Fra det ene til det andre 

<style>.step2 > #p16 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(28% 11%, 26% 11%, 29% 6%);
  clip-path: polygon(28% 11%, 26% 11%, 29% 6%);
}

.step1 > #p39 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(57% 70%, 60% 70%, 73% 60%);
  clip-path: polygon(57% 70%, 60% 70%, 73% 60%);
}

.step1 > #p102 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(75% 77%, 77% 79%, 82% 78%);
  clip-path: polygon(75% 77%, 77% 79%, 82% 78%);
}

.step1 > #p62 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(5% 75%, 13% 77%, 15% 74%);
  clip-path: polygon(5% 75%, 13% 77%, 15% 74%);
}

.step1 > #p81 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(35% 70%, 39% 74%, 33% 76%);
  clip-path: polygon(35% 70%, 39% 74%, 33% 76%);
}

.polygon { 
  transition: all 2s ease;
  position: absolute;
}

.step2 > #p55 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(29% 36%, 29% 40%, 34% 45%);
  clip-path: polygon(29% 36%, 29% 40%, 34% 45%);
}

.step1 > #p46 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(23% 90%, 80% 90%, 62% 87%);
  clip-path: polygon(23% 90%, 80% 90%, 62% 87%);
}

.step2 > #p71 { 
  width: 300px;
  height: 300px;
  background-color: #ffd2ac;
  -webkit-clip-path: polygon(65% 45%, 59% 43%, 62% 46%);
  clip-path: polygon(65% 45%, 59% 43%, 62% 46%);
}

.step1 > #p73 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(17% 71%, 20% 76%, 24% 72%);
  clip-path: polygon(17% 71%, 20% 76%, 24% 72%);
}

.step1 > #p57 { 
  width: 300px;
  height: 300px;
  background-color: #251a11;
  -webkit-clip-path: polygon(53% 96%, 53% 100%, 59% 95%);
  clip-path: polygon(53% 96%, 53% 100%, 59% 95%);
}

.step2 > #p17 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(38.5% 28%, 38% 31%, 30% 25%);
  clip-path: polygon(38.5% 28%, 38% 31%, 30% 25%);
}

.step1 > #p10 { 
  width: 300px;
  height: 300px;
  background-color: #2B9720;
  -webkit-clip-path: polygon(68% 43%, 68.9% 0%, 75% 32%);
  clip-path: polygon(68% 43%, 68.9% 0%, 75% 32%);
}

.step2 > #p41 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(40% 39%, 43% 37%, 41% 36%);
  clip-path: polygon(40% 39%, 43% 37%, 41% 36%);
}

.step2 > #p26 { 
  width: 300px;
  height: 300px;
  background-color: grey;
  -webkit-clip-path: polygon(23% 22%, 29% 22%, 21% 20%);
  clip-path: polygon(23% 22%, 29% 22%, 21% 20%);
}

.step2 > #p98 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(53% 62%, 55% 61%, 50% 55%);
  clip-path: polygon(53% 62%, 55% 61%, 50% 55%);
}

.step1 > #p42 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(57% 70%, 49% 89%, 59% 88%);
  clip-path: polygon(57% 70%, 49% 89%, 59% 88%);
}

.step2 > #p42 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(40% 39%, 43% 37%, 44% 47%);
  clip-path: polygon(40% 39%, 43% 37%, 44% 47%);
}

.step1 > #p40 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(57% 70%, 60% 70%, 62% 85%);
  clip-path: polygon(57% 70%, 60% 70%, 62% 85%);
}

.step2 > #p102 { 
  width: 300px;
  height: 300px;
  background-color: black;
  -webkit-clip-path: polygon(47% 55%, 48% 59%, 53% 55%);
  clip-path: polygon(47% 55%, 48% 59%, 53% 55%);
}

.step1 > #p70 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(20% 76%, 25% 81%, 26% 77%);
  clip-path: polygon(20% 76%, 25% 81%, 26% 77%);
}

.step2 > #p21 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(39% 19%, 41% 34%, 41% 20%);
  clip-path: polygon(39% 19%, 41% 34%, 41% 20%);
}

.step1 > #p108 { 
  width: 300px;
  height: 300px;
  background-color: #4d4d4d;
  -webkit-clip-path: polygon(84% 80%, 88% 77%, 85% 77%);
  clip-path: polygon(84% 80%, 88% 77%, 85% 77%);
}

.step1 > #p55 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(69% 94%, 59% 95%, 62% 98%);
  clip-path: polygon(69% 94%, 59% 95%, 62% 98%);
}

.step1 > #p31 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(53% 53%, 55% 67%, 54% 53%);
  clip-path: polygon(53% 53%, 55% 67%, 54% 53%);
}

.step1 > #p2 { 
  width: 300px;
  height: 300px;
  background-color: #2B9720;
  -webkit-clip-path: polygon(16% 24%, 21% 30%, 31% 25%);
  clip-path: polygon(16% 24%, 21% 30%, 31% 25%);
}

.step2 > #p29 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(59% 34%, 62% 31%, 61% 19%);
  clip-path: polygon(59% 34%, 62% 31%, 61% 19%);
}

.step1 > #p5 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(26% 20%, 35% 29%, 61% 35%);
  clip-path: polygon(26% 20%, 35% 29%, 61% 35%);
}

.step1 > #p107 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(84% 74%, 88% 77%, 85% 77%);
  clip-path: polygon(84% 74%, 88% 77%, 85% 77%);
}

.step1 > #p27 { 
  width: 300px;
  height: 300px;
  background-color: #185112;
  -webkit-clip-path: polygon(58% 49%, 49% 56%, 24% 46%);
  clip-path: polygon(58% 49%, 49% 56%, 24% 46%);
}

.step1 > #p8 { 
  width: 300px;
  height: 300px;
  background-color: #2B9720;
  -webkit-clip-path: polygon(35% 29%, 24% 46%, 61% 35%);
  clip-path: polygon(35% 29%, 24% 46%, 61% 35%);
}

.step2 > #p67 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(62% 37%, 72% 35%, 70% 36%);
  clip-path: polygon(62% 37%, 72% 35%, 70% 36%);
}

.step1 > #p68 { 
  width: 300px;
  height: 300px;
  background-color: #3e3e3e;
  -webkit-clip-path: polygon(15% 74%, 16% 78%, 20% 76%);
  clip-path: polygon(15% 74%, 16% 78%, 20% 76%);
}

.step2 > #p24 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(21% 20%, 23% 15%, 23% 21%);
  clip-path: polygon(21% 20%, 23% 15%, 23% 21%);
}

.step2 > #p65 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(59% 43%, 59% 39%, 62% 37%);
  clip-path: polygon(59% 43%, 59% 39%, 62% 37%);
}

.step1 > #p29 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(23% 57%, 35% 50%, 35% 62%);
  clip-path: polygon(23% 57%, 35% 50%, 35% 62%);
}

.step1 > #p19 { 
  width: 300px;
  height: 300px;
  background-color: #64B35C;
  -webkit-clip-path: polygon(66% 53%, 84% 50%, 79% 66%);
  clip-path: polygon(66% 53%, 84% 50%, 79% 66%);
}

.step2 > #p58 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(40% 39%, 40% 43%, 42% 43%);
  clip-path: polygon(40% 39%, 40% 43%, 42% 43%);
}

.step2 > #p20 { 
  width: 300px;
  height: 300px;
  background-color: grey;
  -webkit-clip-path: polygon(41% 36.5%, 44% 35%, 41% 28%);
  clip-path: polygon(41% 36.5%, 44% 35%, 41% 28%);
}

.step2 > #p48 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(53.5% 40%, 54% 36%, 57% 37%);
  clip-path: polygon(53.5% 40%, 54% 36%, 57% 37%);
}

.step1 > #p48 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(36% 94%, 42% 90%, 47% 95%);
  clip-path: polygon(36% 94%, 42% 90%, 47% 95%);
}

.step2 > #p22 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(39% 19%, 41% 34%, 38% 31%);
  clip-path: polygon(39% 19%, 41% 34%, 38% 31%);
}

.step2 > #p43 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(43% 37%, 44% 47%, 46.5% 40%);
  clip-path: polygon(43% 37%, 44% 47%, 46.5% 40%);
}

.step1 > #p7 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(26% 20%, 69% 0%, 61% 35%);
  clip-path: polygon(26% 20%, 69% 0%, 61% 35%);
}

#app { 
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.step2 > #p47 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(57% 37%, 59% 37%, 59% 39%);
  clip-path: polygon(57% 37%, 59% 37%, 59% 39%);
}

.step2 > #p51 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(27% 35%, 29% 36%, 38% 37%);
  clip-path: polygon(27% 35%, 29% 36%, 38% 37%);
}

.step2 > #p84 { 
  width: 300px;
  height: 300px;
  background-color: #3e2c1d;
  -webkit-clip-path: polygon(55% 57%, 59% 59%, 55% 61%);
  clip-path: polygon(55% 57%, 59% 59%, 55% 61%);
}

.step2 > #p60 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(37% 46%, 40% 46%, 42% 43%);
  clip-path: polygon(37% 46%, 40% 46%, 42% 43%);
}

.container {
  margin: 0 auto; 
  width: 400px;
  height: 300px;
  position: relative;
}

.step2 > #p78 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(59% 50%, 55% 52%, 59% 54%);
  clip-path: polygon(59% 50%, 55% 52%, 59% 54%);
}

.step2 > #p87 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(48% 64%, 52% 64%, 53% 62%);
  clip-path: polygon(48% 64%, 52% 64%, 53% 62%);
}

.step1 > #p14 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(84% 38%, 90% 40%, 91% 27%);
  clip-path: polygon(84% 38%, 90% 40%, 91% 27%);
}

.step1 > #p86 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(89% 79%, 86% 81%, 90% 81%);
  clip-path: polygon(89% 79%, 86% 81%, 90% 81%);
}

.step2 > #p6 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(40% 59%, 38% 81%, 43% 63%);
  clip-path: polygon(40% 59%, 38% 81%, 43% 63%);
}

.step2 > #p90 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(50% 35%, 47% 46%, 53% 46%);
  clip-path: polygon(50% 35%, 47% 46%, 53% 46%);
}

.step1 > #p34 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(38% 55%, 49% 59%, 49% 64%);
  clip-path: polygon(38% 55%, 49% 59%, 49% 64%);
}

.step2 > #p56 { 
  width: 300px;
  height: 300px;
  background-color: #ffd2ac;
  -webkit-clip-path: polygon(29% 36%, 34% 45%, 40% 43%);
  clip-path: polygon(29% 36%, 34% 45%, 40% 43%);
}

.step2 > #p76 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(59% 50%, 62% 46%, 57% 47%);
  clip-path: polygon(59% 50%, 62% 46%, 57% 47%);
}

.step1 > #p1 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(16% 24%, 15% 32%, 21% 30%);
  clip-path: polygon(16% 24%, 15% 32%, 21% 30%);
}

.step1 > #p17 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(68% 43%, 62% 44%, 66% 53%);
  clip-path: polygon(68% 43%, 62% 44%, 66% 53%);
}

.step1 > #p6 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(5% 36%, 24% 53%, 27% 43%);
  clip-path: polygon(5% 36%, 24% 53%, 27% 43%);
}

.step1 > #p23 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(61% 35%, 68% 43%, 62% 44%);
  clip-path: polygon(61% 35%, 68% 43%, 62% 44%);
}

.step2 > #p3 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(41.5% 68%, 38% 81%, 50% 96%);
  clip-path: polygon(41.5% 68%, 38% 81%, 50% 96%);
}

.step1 > #p92 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(68% 78%, 72% 79%, 73% 80%);
  clip-path: polygon(68% 78%, 72% 79%, 73% 80%);
}

.step1 > #p88 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(86% 81%, 90% 81%, 89% 82%);
  clip-path: polygon(86% 81%, 90% 81%, 89% 82%);
}

.step2 > #p19 { 
  width: 300px;
  height: 300px;
  background-color: grey;
  -webkit-clip-path: polygon(41% 36.5%, 43% 37%, 44% 35%);
  clip-path: polygon(41% 36.5%, 43% 37%, 44% 35%);
}

.step1 > #p59 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(47% 95%, 53% 100%, 44% 98%);
  clip-path: polygon(47% 95%, 53% 100%, 44% 98%);
}

.step2 > #p72 { 
  width: 300px;
  height: 300px;
  background-color: #ffd2ac;
  -webkit-clip-path: polygon(59% 43%, 65% 45%, 70% 36%);
  clip-path: polygon(59% 43%, 65% 45%, 70% 36%);
}

.step1 > #p85 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(29% 75%, 33% 76%, 30% 78%);
  clip-path: polygon(29% 75%, 33% 76%, 30% 78%);
}

.step1 > #p66 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(12% 69%, 15% 74%, 17% 71%);
  clip-path: polygon(12% 69%, 15% 74%, 17% 71%);
}

.step1 > #p35 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(38% 55%, 49% 61%, 49% 64%);
  clip-path: polygon(38% 55%, 49% 61%, 49% 64%);
}

.step2 > #p37 { 
  width: 300px;
  height: 300px;
  background-color: #ffc38b;
  -webkit-clip-path: polygon(45% 52%, 47% 46%, 46.5% 40%);
  clip-path: polygon(45% 52%, 47% 46%, 46.5% 40%);
}

.step1 > #p56 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(59% 95%, 62% 98%, 53% 100%);
  clip-path: polygon(59% 95%, 62% 98%, 53% 100%);
}

.step2 > #p32 { 
  width: 300px;
  height: 300px;
  background-color: grey;
  -webkit-clip-path: polygon(56% 35%, 57% 37%, 59% 37%);
  clip-path: polygon(56% 35%, 57% 37%, 59% 37%);
}

.step1 > #p21 { 
  width: 300px;
  height: 300px;
  background-color: #10370C;
  -webkit-clip-path: polygon(53% 52.9%, 66.1% 52.9%, 59% 60%);
  clip-path: polygon(53% 52.9%, 66.1% 52.9%, 59% 60%);
}

.step1 > #p93 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(71% 74%, 72% 78%, 75% 77%);
  clip-path: polygon(71% 74%, 72% 78%, 75% 77%);
}

.step2 > #p10 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(70% 22%, 73% 11%, 75% 11%);
  clip-path: polygon(70% 22%, 73% 11%, 75% 11%);
}

.step2 > #p93 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(53% 46%, 55% 52%, 50% 55%);
  clip-path: polygon(53% 46%, 55% 52%, 50% 55%);
}

.step2 > #p33 { 
  width: 300px;
  height: 300px;
  background-color: grey;
  -webkit-clip-path: polygon(56% 35%, 59% 37%, 59% 28%);
  clip-path: polygon(56% 35%, 59% 37%, 59% 28%);
}

.step2 > #p100 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(56% 57%, 55% 52%, 50% 55%);
  clip-path: polygon(56% 57%, 55% 52%, 50% 55%);
}

.step2 > #p99 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(55% 61%, 56% 57%, 50% 55%);
  clip-path: polygon(55% 61%, 56% 57%, 50% 55%);
}

.step2 > #p70 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(70% 36%, 70% 40%, 65% 45%);
  clip-path: polygon(70% 36%, 70% 40%, 65% 45%);
}

.step1 > #p58 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(53% 96%, 53% 100%, 47% 95%);
  clip-path: polygon(53% 96%, 53% 100%, 47% 95%);
}

.step2 > #p36 { 
  width: 300px;
  height: 300px;
  background-color: grey;
  -webkit-clip-path: polygon(70% 22%, 76% 22%, 77% 19.5%);
  clip-path: polygon(70% 22%, 76% 22%, 77% 19.5%);
}

.step2 > #p80 { 
  width: 300px;
  height: 300px;
  background-color: #3e2c1d;
  -webkit-clip-path: polygon(40% 54%, 44% 57%, 40% 59%);
  clip-path: polygon(40% 54%, 44% 57%, 40% 59%);
}

.step2 > #p52 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(29% 36%, 38% 37%, 40% 43%);
  clip-path: polygon(29% 36%, 38% 37%, 40% 43%);
}

.step1 > #p90 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(68% 78%, 72% 78%, 71% 74%);
  clip-path: polygon(68% 78%, 72% 78%, 71% 74%);
}

.step1 > #p97 { 
  width: 300px;
  height: 300px;
  background-color: #3e3e3e;
  -webkit-clip-path: polygon(72% 79%, 73% 80%, 75% 77%);
  clip-path: polygon(72% 79%, 73% 80%, 75% 77%);
}

.step1 > #p60 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(47% 95%, 44% 98%, 36% 94%);
  clip-path: polygon(47% 95%, 44% 98%, 36% 94%);
}

.step2 > #p66 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(59% 43%, 62% 37%, 70% 36%);
  clip-path: polygon(59% 43%, 62% 37%, 70% 36%);
}

.step2 > #p49 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(53.5% 40%, 55% 47%, 57% 37%);
  clip-path: polygon(53.5% 40%, 55% 47%, 57% 37%);
}

.step1 > #p26 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(61% 35%, 58% 49%, 24% 46%);
  clip-path: polygon(61% 35%, 58% 49%, 24% 46%);
}

.step2 > #p62 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(40% 50%, 45% 52%, 42% 46%);
  clip-path: polygon(40% 50%, 45% 52%, 42% 46%);
}

.step2 > #p77 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(59% 50%, 55% 52%, 57% 47%);
  clip-path: polygon(59% 50%, 55% 52%, 57% 47%);
}

.step1 > #p72 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(17% 71%, 24% 67%, 24% 72%);
  clip-path: polygon(17% 71%, 24% 67%, 24% 72%);
}

.step2 > #p89 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(50% 35%, 54% 36%, 53% 46%);
  clip-path: polygon(50% 35%, 54% 36%, 53% 46%);
}

.step1 > #p3 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(5% 36%, 31% 25%, 23% 52%);
  clip-path: polygon(5% 36%, 31% 25%, 23% 52%);
}

.step1 > #p106 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(80% 75%, 82% 78%, 84% 74%);
  clip-path: polygon(80% 75%, 82% 78%, 84% 74%);
}

.step1 > #p61 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(5% 75%, 12% 69%, 15% 74%);
  clip-path: polygon(5% 75%, 12% 69%, 15% 74%);
}

.step1 > #p94 { 
  width: 300px;
  height: 300px;
  background-color: #4d4d4d;
  -webkit-clip-path: polygon(75% 77%, 72% 79%, 72% 78%);
  clip-path: polygon(75% 77%, 72% 79%, 72% 78%);
}

.step1 > #p28 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(35% 50%, 39% 52%, 35% 62%);
  clip-path: polygon(35% 50%, 39% 52%, 35% 62%);
}

.step1 > #p95 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(72% 78%, 72% 79%, 75% 77%);
  clip-path: polygon(72% 78%, 72% 79%, 75% 77%);
}

.step1 > #p16 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(75% 32%, 94% 41%, 66% 53%);
  clip-path: polygon(75% 32%, 94% 41%, 66% 53%);
}

.step1 > #p91 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(68% 78%, 72% 78%, 72% 79%);
  clip-path: polygon(68% 78%, 72% 78%, 72% 79%);
}

.step2 > #p5 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(40% 59%, 35% 70%, 38% 81%);
  clip-path: polygon(40% 59%, 35% 70%, 38% 81%);
}

.step2 > #p18 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(38.5% 28%, 30% 25%, 29% 22%);
  clip-path: polygon(38.5% 28%, 30% 25%, 29% 22%);
}

.step1 > #p69 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(16% 78%, 20% 76%, 25% 81%);
  clip-path: polygon(16% 78%, 20% 76%, 25% 81%);
}

.step1 > #p78 { 
  width: 300px;
  height: 300px;
  background-color: #4d4d4d;
  -webkit-clip-path: polygon(24% 72%, 26% 77%, 29% 75%);
  clip-path: polygon(24% 72%, 26% 77%, 29% 75%);
}

.step1 > #p103 { 
  width: 300px;
  height: 300px;
  background-color: #4d4d4d;
  -webkit-clip-path: polygon(77% 79%, 82% 78%, 84% 80%);
  clip-path: polygon(77% 79%, 82% 78%, 84% 80%);
}

.step2 > #p4 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(61% 81%, 50% 96%, 57.5% 68%);
  clip-path: polygon(61% 81%, 50% 96%, 57.5% 68%);
}

.step2 > #p63 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(40% 50%, 45% 52%, 40% 54%);
  clip-path: polygon(40% 50%, 45% 52%, 40% 54%);
}

.step1 > #p101 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(75% 77%, 80% 75%, 82% 78%);
  clip-path: polygon(75% 77%, 80% 75%, 82% 78%);
}

.step2 > #p64 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(40% 54%, 45% 52%, 44% 57%);
  clip-path: polygon(40% 54%, 45% 52%, 44% 57%);
}

.step1 > #p32 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(53% 53%, 55% 67%, 52% 55%);
  clip-path: polygon(53% 53%, 55% 67%, 52% 55%);
}

.step2 > #p13 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(71% 5%, 70% 6%, 65% 1%);
  clip-path: polygon(71% 5%, 70% 6%, 65% 1%);
}

.step2 > #p91 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(47% 46%, 53% 46%, 50% 55%);
  clip-path: polygon(47% 46%, 53% 46%, 50% 55%);
}

.step2 > #p54 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(27% 35%, 29% 36%, 29% 40%);
  clip-path: polygon(27% 35%, 29% 36%, 29% 40%);
}

.step2 > #p83 { 
  width: 300px;
  height: 300px;
  background-color: #3e2c1d;
  -webkit-clip-path: polygon(55% 57%, 59% 59%, 59% 54%);
  clip-path: polygon(55% 57%, 59% 59%, 59% 54%);
}

.step2 > #p25 { 
  width: 300px;
  height: 300px;
  background-color: grey;
  -webkit-clip-path: polygon(30% 25%, 29% 22%, 23% 22%);
  clip-path: polygon(30% 25%, 29% 22%, 23% 22%);
}

.step2 > #p108 { 
  width: 300px;
  height: 300px;
  background-color: black;
  -webkit-clip-path: polygon(56.5% 46%, 57.5% 49%, 58.5% 46%);
  clip-path: polygon(56.5% 46%, 57.5% 49%, 58.5% 46%);
}

.step2 > #p46 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(56% 35%, 54% 36%, 57% 37%);
  clip-path: polygon(56% 35%, 54% 36%, 57% 37%);
}

.step1 > #p64 { 
  width: 300px;
  height: 300px;
  background-color: #3e3e3e;
  -webkit-clip-path: polygon(11% 79%, 13% 77%, 16% 78%);
  clip-path: polygon(11% 79%, 13% 77%, 16% 78%);
}

.step2 > #p107 { 
  width: 300px;
  height: 300px;
  background-color: black;
  -webkit-clip-path: polygon(55.5% 49%, 57.5% 49%, 56.5% 46%);
  clip-path: polygon(55.5% 49%, 57.5% 49%, 56.5% 46%);
}

.step2 > #p11 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(75% 11%, 73% 11%, 71% 5%);
  clip-path: polygon(75% 11%, 73% 11%, 71% 5%);
}

.step2 > #p45 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(43% 37%, 44% 35%, 46% 36%);
  clip-path: polygon(43% 37%, 44% 35%, 46% 36%);
}

.step2 > #p68 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(62% 37%, 72% 35%, 70% 36%);
  clip-path: polygon(62% 37%, 72% 35%, 70% 36%);
}

.step2 > #p23 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(39% 19%, 41% 20%, 44% 15%);
  clip-path: polygon(39% 19%, 41% 20%, 44% 15%);
}

.step1 > #p18 { 
  width: 300px;
  height: 300px;
  background-color: #2B9720;
  -webkit-clip-path: polygon(66% 53%, 84% 50%, 94% 41%);
  clip-path: polygon(66% 53%, 84% 50%, 94% 41%);
}

.step2 > #p104 { 
  width: 300px;
  height: 300px;
  background-color: black;
  -webkit-clip-path: polygon(53% 55%, 54% 57%, 52% 59%);
  clip-path: polygon(53% 55%, 54% 57%, 52% 59%);
}

.step2 > #p35 { 
  width: 300px;
  height: 300px;
  background-color: grey;
  -webkit-clip-path: polygon(70% 22%, 69% 25%, 76% 22%);
  clip-path: polygon(70% 22%, 69% 25%, 76% 22%);
}

.step1 > #p38 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(55% 67%, 57% 70%, 73% 60%);
  clip-path: polygon(55% 67%, 57% 70%, 73% 60%);
}

.step1 > #p54 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(65% 90%, 69% 94%, 80% 90%);
  clip-path: polygon(65% 90%, 69% 94%, 80% 90%);
}

.step2 > #p2 { 
  width: 300px;
  height: 300px;
  background-color: #251a11;
  -webkit-clip-path: polygon(41.5% 68%, 57.5% 68%, 50% 36%);
  clip-path: polygon(41.5% 68%, 57.5% 68%, 50% 36%);
}

.step1 > #p22 { 
  width: 300px;
  height: 300px;
  background-color: #10370C;
  -webkit-clip-path: polygon(52% 53%, 62% 44%, 66% 53%);
  clip-path: polygon(52% 53%, 62% 44%, 66% 53%);
}

.step1 > #p12 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(80% 28%, 81% 31%, 91% 27%);
  clip-path: polygon(80% 28%, 81% 31%, 91% 27%);
}

.step1 > #p24 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(61% 35%, 58% 49%, 62% 44%);
  clip-path: polygon(61% 35%, 58% 49%, 62% 44%);
}

.step1 > #p77 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(35% 70%, 33% 76%, 29% 75%);
  clip-path: polygon(35% 70%, 33% 76%, 29% 75%);
}

.step1 > #p82 { 
  width: 300px;
  height: 300px;
  background-color: #4d4d4d;
  -webkit-clip-path: polygon(39% 74%, 33% 76%, 37% 77%);
  clip-path: polygon(39% 74%, 33% 76%, 37% 77%);
}

.step2 > #p28 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(61.5% 28%, 70% 22%, 69% 25%);
  clip-path: polygon(61.5% 28%, 70% 22%, 69% 25%);
}

.step1 > #p100 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(75% 77%, 77% 73%, 80% 75%);
  clip-path: polygon(75% 77%, 77% 73%, 80% 75%);
}

.step2 > #p101 { 
  width: 300px;
  height: 300px;
  background-color: black;
  -webkit-clip-path: polygon(47% 55%, 48% 59%, 46% 57%);
  clip-path: polygon(47% 55%, 48% 59%, 46% 57%);
}

.step1 > #p30 { 
  width: 300px;
  height: 300px;
  background-color: #10370C;
  -webkit-clip-path: polygon(39% 52%, 35% 62%, 49% 56%);
  clip-path: polygon(39% 52%, 35% 62%, 49% 56%);
}

.step2 > #p88 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(50% 35%, 46% 36%, 47% 46%);
  clip-path: polygon(50% 35%, 46% 36%, 47% 46%);
}

.step2 > #p106 { 
  width: 300px;
  height: 300px;
  background-color: black;
  -webkit-clip-path: polygon(41.5% 49%, 40.5% 46%, 42.5% 46%);
  clip-path: polygon(41.5% 49%, 40.5% 46%, 42.5% 46%);
}

.step1 > #p89 { 
  width: 300px;
  height: 300px;
  background-color: #4d4d4d;
  -webkit-clip-path: polygon(89% 82%, 90% 81%, 92% 80%);
  clip-path: polygon(89% 82%, 90% 81%, 92% 80%);
}

.step2 > #p44 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(43% 37%, 46.5% 40%, 46% 36%);
  clip-path: polygon(43% 37%, 46.5% 40%, 46% 36%);
}

.step1 > #p37 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(55% 67%, 57% 70%, 49% 90%);
  clip-path: polygon(55% 67%, 57% 70%, 49% 90%);
}

.step2 > #p86 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(47% 62%, 48% 64%, 53% 62%);
  clip-path: polygon(47% 62%, 48% 64%, 53% 62%);
}

.step1 > #p104 { 
  width: 300px;
  height: 300px;
  background-color: #3e3e3e;
  -webkit-clip-path: polygon(82% 78%, 85% 77%, 84% 80%);
  clip-path: polygon(82% 78%, 85% 77%, 84% 80%);
}

.step1 > #p15 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(66% 53%, 67.9% 43%, 75.1% 32%);
  clip-path: polygon(66% 53%, 67.9% 43%, 75.1% 32%);
}

.step1 > #p33 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(52.5% 54%, 55% 67%, 49% 90%);
  clip-path: polygon(52.5% 54%, 55% 67%, 49% 90%);
}

.step2 > #p97 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(47% 62%, 53% 62%, 50% 55%);
  clip-path: polygon(47% 62%, 53% 62%, 50% 55%);
}

.step1 > #p99 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(71% 74%, 74% 75%, 75% 77%);
  clip-path: polygon(71% 74%, 74% 75%, 75% 77%);
}

.step2 > #p12 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(73% 11%, 70% 6%, 71% 5%);
  clip-path: polygon(73% 11%, 70% 6%, 71% 5%);
}

.step1 > #p50 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(47% 95%, 53% 96%, 53% 90%);
  clip-path: polygon(47% 95%, 53% 96%, 53% 90%);
}

.step1 > #p83 { 
  width: 300px;
  height: 300px;
  background-color: #3e3e3e;
  -webkit-clip-path: polygon(37% 77%, 34% 79%, 33% 76%);
  clip-path: polygon(37% 77%, 34% 79%, 33% 76%);
}

.step2 > #p40 { 
  width: 300px;
  height: 300px;
  background-color: #ffc38b;
  -webkit-clip-path: polygon(55% 52%, 53.5% 40%, 61% 46%);
  clip-path: polygon(55% 52%, 53.5% 40%, 61% 46%);
}

.step2 > #p14 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(32% 24%, 29% 22%, 28% 11%);
  clip-path: polygon(32% 24%, 29% 22%, 28% 11%);
}

.step1 > #p65 { 
  width: 300px;
  height: 300px;
  background-color: #4d4d4d;
  -webkit-clip-path: polygon(13% 77%, 16% 78%, 15% 74%);
  clip-path: polygon(13% 77%, 16% 78%, 15% 74%);
}

.step1 > #p84 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(30% 78%, 34% 79%, 33% 76%);
  clip-path: polygon(30% 78%, 34% 79%, 33% 76%);
}

.step2 > #p8 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(59% 59%, 61% 81%, 64% 70%);
  clip-path: polygon(59% 59%, 61% 81%, 64% 70%);
}

.step1 > #p67 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(15% 74%, 20% 76%, 17% 71%);
  clip-path: polygon(15% 74%, 20% 76%, 17% 71%);
}

.step2 > #p75 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(57% 43%, 62% 46%, 60% 46.5%);
  clip-path: polygon(57% 43%, 62% 46%, 60% 46.5%);
}

.step2 > #p95 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(44% 57%, 45% 61%, 50% 55%);
  clip-path: polygon(44% 57%, 45% 61%, 50% 55%);
}

.step2 > #p31 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(59% 20%, 61% 19%, 57% 15%);
  clip-path: polygon(59% 20%, 61% 19%, 57% 15%);
}

.step1 > #p51 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(53% 90%, 53% 96%, 59% 95%);
  clip-path: polygon(53% 90%, 53% 96%, 59% 95%);
}

.step1 > #p49 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(42% 90%, 53% 90%, 47% 95%);
  clip-path: polygon(42% 90%, 53% 90%, 47% 95%);
}

.step2 > #p59 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(40% 43%, 42% 43%, 37% 46%);
  clip-path: polygon(40% 43%, 42% 43%, 37% 46%);
}

.step1 > #p41 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(57% 70%, 59% 88%, 63% 88%);
  clip-path: polygon(57% 70%, 59% 88%, 63% 88%);
}

.step2 > #p38 { 
  width: 300px;
  height: 300px;
  background-color: #ffc38b;
  -webkit-clip-path: polygon(45% 52%, 46.5% 40%, 38% 45%);
  clip-path: polygon(45% 52%, 46.5% 40%, 38% 45%);
}

.step2 > #p7 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(59% 59%, 61% 81%, 56% 63%);
  clip-path: polygon(59% 59%, 61% 81%, 56% 63%);
}

.step2 > #p96 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(47% 62%, 45% 61%, 50% 55%);
  clip-path: polygon(47% 62%, 45% 61%, 50% 55%);
}

.step1 > #p96 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(73% 80%, 75% 77%, 77% 79%);
  clip-path: polygon(73% 80%, 75% 77%, 77% 79%);
}

.step2 > #p9 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(66% 25%, 70% 22%, 73% 11%);
  clip-path: polygon(66% 25%, 70% 22%, 73% 11%);
}

.step1 > #p63 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(5% 75%, 11% 79%, 13% 77%);
  clip-path: polygon(5% 75%, 11% 79%, 13% 77%);
}

.step2 > #p82 { 
  width: 300px;
  height: 300px;
  background-color: #3e2c1d;
  -webkit-clip-path: polygon(40% 59%, 43% 63%, 47% 61%);
  clip-path: polygon(40% 59%, 43% 63%, 47% 61%);
}

.step1 > #p36 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(53% 53%, 49% 56%, 49% 90%);
  clip-path: polygon(53% 53%, 49% 56%, 49% 90%);
}

.step1 > #p87 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(89% 79%, 92% 80%, 90% 81%);
  clip-path: polygon(89% 79%, 92% 80%, 90% 81%);
}

.step2 > #p50 { 
  width: 300px;
  height: 300px;
  background-color: #d37227;
  -webkit-clip-path: polygon(55% 47%, 57% 37%, 59% 39%);
  clip-path: polygon(55% 47%, 57% 37%, 59% 39%);
}

.step2 > #p30 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(59% 34%, 59% 20%, 61% 19%);
  clip-path: polygon(59% 34%, 59% 20%, 61% 19%);
}

.step2 > #p85 { 
  width: 300px;
  height: 300px;
  background-color: #3e2c1d;
  -webkit-clip-path: polygon(59% 59%, 56% 63%, 51% 60%);
  clip-path: polygon(59% 59%, 56% 63%, 51% 60%);
}

.step2 > #p53 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(38% 37%, 40% 39%, 40% 43%);
  clip-path: polygon(38% 37%, 40% 39%, 40% 43%);
}

.step2 > #p73 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(59% 39%, 57% 43%, 59% 43%);
  clip-path: polygon(59% 39%, 57% 43%, 59% 43%);
}

.step2 > #p105 { 
  width: 300px;
  height: 300px;
  background-color: black;
  -webkit-clip-path: polygon(41.5% 49%, 43.5% 49%, 42.5% 46%);
  clip-path: polygon(41.5% 49%, 43.5% 49%, 42.5% 46%);
}

.step2 > #p1 { 
  width: 300px;
  height: 300px;
  background-color: #251a11;
  -webkit-clip-path: polygon(41.5% 68%, 50% 96%, 57.5% 68%);
  clip-path: polygon(41.5% 68%, 50% 96%, 57.5% 68%);
}

.step2 > #p15 { 
  width: 300px;
  height: 300px;
  background-color: darkgrey;
  -webkit-clip-path: polygon(29% 22%, 28% 11%, 26% 11%);
  clip-path: polygon(29% 22%, 28% 11%, 26% 11%);
}

.step1 > #p105 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(82% 78%, 85% 77%, 84% 74%);
  clip-path: polygon(82% 78%, 85% 77%, 84% 74%);
}

.step1 > #p20 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(66% 53%, 79% 66%, 59% 60%);
  clip-path: polygon(66% 53%, 79% 66%, 59% 60%);
}

.step2 > #p61 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(37% 46%, 40% 50%, 42% 46%);
  clip-path: polygon(37% 46%, 40% 50%, 42% 46%);
}

.step2 > #p94 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(45% 52%, 44% 57%, 50% 55%);
  clip-path: polygon(45% 52%, 44% 57%, 50% 55%);
}

.step1 > #p45 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(50% 89%, 53% 90%, 59% 88%);
  clip-path: polygon(50% 89%, 53% 90%, 59% 88%);
}

.step2 > #p57 { 
  width: 300px;
  height: 300px;
  background-color: #ffd2ac;
  -webkit-clip-path: polygon(34% 45%, 40% 43%, 37% 46%);
  clip-path: polygon(34% 45%, 40% 43%, 37% 46%);
}

.step2 > #p39 { 
  width: 300px;
  height: 300px;
  background-color: #ffc38b;
  -webkit-clip-path: polygon(53.5% 40%, 53% 46%, 55% 52%);
  clip-path: polygon(53.5% 40%, 53% 46%, 55% 52%);
}

.step1 > #p44 { 
  width: 300px;
  height: 300px;
  background-color: #185112;
  -webkit-clip-path: polygon(32% 90%, 44% 87%, 53% 90%);
  clip-path: polygon(32% 90%, 44% 87%, 53% 90%);
}

.step1 > #p9 { 
  width: 300px;
  height: 300px;
  background-color: #2B9720;
  -webkit-clip-path: polygon(61% 35%, 68% 43%, 69% 0%);
  clip-path: polygon(61% 35%, 68% 43%, 69% 0%);
}

.step2 > #p81 { 
  width: 300px;
  height: 300px;
  background-color: #3e2c1d;
  -webkit-clip-path: polygon(40% 59%, 44% 57%, 45% 61%);
  clip-path: polygon(40% 59%, 44% 57%, 45% 61%);
}

.step1 > #p74 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(24% 67%, 30% 71%, 24% 72%);
  clip-path: polygon(24% 67%, 30% 71%, 24% 72%);
}

.step2 > #p69 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(70% 36%, 70% 40%, 72% 35%);
  clip-path: polygon(70% 36%, 70% 40%, 72% 35%);
}

.step1 > #p76 { 
  width: 300px;
  height: 300px;
  background-color: #a0a0a0;
  -webkit-clip-path: polygon(30% 71%, 29% 75%, 35% 70%);
  clip-path: polygon(30% 71%, 29% 75%, 35% 70%);
}

.step2 > #p92 { 
  width: 300px;
  height: 300px;
  background-color: #fca266;
  -webkit-clip-path: polygon(47% 46%, 45% 52%, 50% 55%);
  clip-path: polygon(47% 46%, 45% 52%, 50% 55%);
}

.step2 > #p103 { 
  width: 300px;
  height: 300px;
  background-color: black;
  -webkit-clip-path: polygon(53% 55%, 48% 59%, 52% 59%);
  clip-path: polygon(53% 55%, 48% 59%, 52% 59%);
}

.step2 > #p27 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(61.5% 28%, 62% 31%, 69% 25%);
  clip-path: polygon(61.5% 28%, 62% 31%, 69% 25%);
}

.step1 > #p53 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(59% 95%, 65% 90%, 69% 94%);
  clip-path: polygon(59% 95%, 65% 90%, 69% 94%);
}

.step1 > #p13 { 
  width: 300px;
  height: 300px;
  background-color: #206E18;
  -webkit-clip-path: polygon(81% 31%, 84% 38%, 91% 27%);
  clip-path: polygon(81% 31%, 84% 38%, 91% 27%);
}

.step1 > #p43 { 
  width: 300px;
  height: 300px;
  background-color: #702e04;
  -webkit-clip-path: polygon(49% 80%, 44% 87%, 50% 89%);
  clip-path: polygon(49% 80%, 44% 87%, 50% 89%);
}

.step1 > #p11 { 
  width: 300px;
  height: 300px;
  background-color: #64B35C;
  -webkit-clip-path: polygon(68.9% 0%, 75% 32%, 84% 38%);
  clip-path: polygon(68.9% 0%, 75% 32%, 84% 38%);
}

.step1 > #p98 { 
  width: 300px;
  height: 300px;
  background-color: #bcbcbc;
  -webkit-clip-path: polygon(74% 75%, 77% 73%, 75% 77%);
  clip-path: polygon(74% 75%, 77% 73%, 75% 77%);
}

.step1 > #p52 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(53% 90%, 59% 95%, 65% 90%);
  clip-path: polygon(53% 90%, 59% 95%, 65% 90%);
}

.step2 > #p34 { 
  width: 300px;
  height: 300px;
  background-color: lightgrey;
  -webkit-clip-path: polygon(77% 19.5%, 75% 21%, 76% 15%);
  clip-path: polygon(77% 19.5%, 75% 21%, 76% 15%);
}

.step1 > #p75 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(24% 72%, 30% 71%, 29% 75%);
  clip-path: polygon(24% 72%, 30% 71%, 29% 75%);
}

.step1 > #p4 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(31% 25%, 24% 46%, 35% 29%);
  clip-path: polygon(31% 25%, 24% 46%, 35% 29%);
}

.step1 > #p47 { 
  width: 300px;
  height: 300px;
  background-color: #8BC685;
  -webkit-clip-path: polygon(24% 90%, 36% 94%, 42% 90%);
  clip-path: polygon(24% 90%, 36% 94%, 42% 90%);
}

.step1 > #p25 { 
  width: 300px;
  height: 300px;
  background-color: #2B9720;
  -webkit-clip-path: polygon(35% 50%, 25% 46%, 23% 57%);
  clip-path: polygon(35% 50%, 25% 46%, 23% 57%);
}

.step1 > #p71 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(26% 77%, 20% 76%, 24% 72%);
  clip-path: polygon(26% 77%, 20% 76%, 24% 72%);
}

.step1 > #p80 { 
  width: 300px;
  height: 300px;
  background-color: #757575;
  -webkit-clip-path: polygon(25% 81%, 30% 78%, 29% 75%);
  clip-path: polygon(25% 81%, 30% 78%, 29% 75%);
}

.step2 > #p79 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(55% 57%, 55% 52%, 59% 54%);
  clip-path: polygon(55% 57%, 55% 52%, 59% 54%);
}

.step2 > #p74 { 
  width: 300px;
  height: 300px;
  background-color: #a55200;
  -webkit-clip-path: polygon(57% 43%, 59% 43%, 62% 46%);
  clip-path: polygon(57% 43%, 59% 43%, 62% 46%);
}

.step1 > #p79 { 
  width: 300px;
  height: 300px;
  background-color: #3e3e3e;
  -webkit-clip-path: polygon(26% 77%, 25% 81%, 29% 75%);
  clip-path: polygon(26% 77%, 25% 81%, 29% 75%);
}
</style>

<div id="root" class="container step1"><div id="p1" class="polygon"></div><div id="p2" class="polygon"></div><div id="p3" class="polygon"></div><div id="p4" class="polygon"></div><div id="p5" class="polygon"></div><div id="p6" class="polygon"></div><div id="p7" class="polygon"></div><div id="p8" class="polygon"></div><div id="p9" class="polygon"></div><div id="p10" class="polygon"></div><div id="p11" class="polygon"></div><div id="p12" class="polygon"></div><div id="p13" class="polygon"></div><div id="p14" class="polygon"></div><div id="p15" class="polygon"></div><div id="p16" class="polygon"></div><div id="p17" class="polygon"></div><div id="p18" class="polygon"></div><div id="p19" class="polygon"></div><div id="p20" class="polygon"></div><div id="p21" class="polygon"></div><div id="p22" class="polygon"></div><div id="p23" class="polygon"></div><div id="p24" class="polygon"></div><div id="p25" class="polygon"></div><div id="p26" class="polygon"></div><div id="p27" class="polygon"></div><div id="p28" class="polygon"></div><div id="p29" class="polygon"></div><div id="p30" class="polygon"></div><div id="p31" class="polygon"></div><div id="p32" class="polygon"></div><div id="p33" class="polygon"></div><div id="p34" class="polygon"></div><div id="p35" class="polygon"></div><div id="p36" class="polygon"></div><div id="p37" class="polygon"></div><div id="p38" class="polygon"></div><div id="p39" class="polygon"></div><div id="p40" class="polygon"></div><div id="p41" class="polygon"></div><div id="p42" class="polygon"></div><div id="p43" class="polygon"></div><div id="p44" class="polygon"></div><div id="p45" class="polygon"></div><div id="p46" class="polygon"></div><div id="p47" class="polygon"></div><div id="p48" class="polygon"></div><div id="p49" class="polygon"></div><div id="p50" class="polygon"></div><div id="p51" class="polygon"></div><div id="p52" class="polygon"></div><div id="p53" class="polygon"></div><div id="p54" class="polygon"></div><div id="p55" class="polygon"></div><div id="p56" class="polygon"></div><div id="p57" class="polygon"></div><div id="p58" class="polygon"></div><div id="p59" class="polygon"></div><div id="p60" class="polygon"></div><div id="p61" class="polygon"></div><div id="p62" class="polygon"></div><div id="p63" class="polygon"></div><div id="p64" class="polygon"></div><div id="p65" class="polygon"></div><div id="p66" class="polygon"></div><div id="p67" class="polygon"></div><div id="p68" class="polygon"></div><div id="p69" class="polygon"></div><div id="p70" class="polygon"></div><div id="p71" class="polygon"></div><div id="p72" class="polygon"></div><div id="p73" class="polygon"></div><div id="p74" class="polygon"></div><div id="p75" class="polygon"></div><div id="p76" class="polygon"></div><div id="p77" class="polygon"></div><div id="p78" class="polygon"></div><div id="p79" class="polygon"></div><div id="p80" class="polygon"></div><div id="p81" class="polygon"></div><div id="p82" class="polygon"></div><div id="p83" class="polygon"></div><div id="p84" class="polygon"></div><div id="p85" class="polygon"></div><div id="p86" class="polygon"></div><div id="p87" class="polygon"></div><div id="p88" class="polygon"></div><div id="p89" class="polygon"></div><div id="p90" class="polygon"></div><div id="p91" class="polygon"></div><div id="p92" class="polygon"></div><div id="p93" class="polygon"></div><div id="p94" class="polygon"></div><div id="p95" class="polygon"></div><div id="p96" class="polygon"></div><div id="p97" class="polygon"></div><div id="p98" class="polygon"></div><div id="p99" class="polygon"></div><div id="p100" class="polygon"></div><div id="p101" class="polygon"></div><div id="p102" class="polygon"></div><div id="p103" class="polygon"></div><div id="p104" class="polygon"></div><div id="p105" class="polygon"></div><div id="p106" class="polygon"></div><div id="p107" class="polygon"></div><div id="p108" class="polygon"></div><div id="p109" class="polygon"></div></div>

<script>



setInterval(function(){
  const root = document.getElementById("root");
  
  if (root.className === "container step1") {
  root.className="container step2";
  } else {
  root.className="container step1";
  }
  
 }, 3000);
</script>


Før du leser videre, hvordan ville du gått frem for å få til dette?
Det er mange måter å lage en slik effekt på, men her er det kun brukt CSS. 
Det eneste som endrer seg mellom tilstandene er navnet på én klasse, resten
fikser nettleseren selv.

## Klippe med CSS
Nettet er i all hovedsak firkantet. Med CSS sin `clip-path` så kan du i praksis klippe
de firkantede boksene akkurat slik du vil. Her er noen enkle eksempler: 

<div style="display: inline-block; margin-left: 10px;background: pink; width: 50px; height: 50px; -webkit-clip-path: circle(20px at 24px 25px); clip-path: circle(20px at 24px 25px);"></div>
<div style="display: inline-block; margin-left: 10px;background: red; width: 50px; height: 50px; -webkit-clip-path: polygon(50% 0%, 100% 50%, 50% 100%, 0% 50%); clip-path: polygon(50% 0%, 100% 50%, 50% 100%, 0% 50%);"></div>
<div style="display: inline-block; margin-left: 10px;background: darkred; width: 50px; height: 50px; -webkit-clip-path: polygon(50% 0%, 100% 50%, 50% 100%); clip-path: polygon(50% 0%, 100% 50%, 50% 100%);"></div>

```html
<div style="clip-path: circle(20px at 24px 25px); ... "></div>
<div style="clip-path: polygon(50% 0%, 100% 50%, 50% 100%, 0% 50%); ..."></div>
<div style="clip-path: polygon(50% 0%, 100% 50%, 50% 100%); ..."></div>
```

Slike former kan man jo lage med SVG, så hva er greia med `clip-path`? Vel, du kan klippe
hva som helst (inkludert SVG-elementer). Et naturlig bruksområde er å klippe bilder, f.eks for
å lage runde profilbilder som er så populært for tiden.  

Bonusen er at `clip-path` er en CSS-verdi som lar seg animere med CSS-transitions. Da har vi alt det vi
trenger.

<style>
  .trekant {width: 100px; height: 100px; background: pink; margin-left: 50px; transition: all 2s ease; font-size: 0.5rem; text-align: center}
  .steg-1 > .trekant {
    clip-path: polygon(0% 0%, 100% 0, 50% 100%);
    -webkit-clip-path: polygon(0 0, 100% 0, 50% 100%);
  }
  .steg-2 > .trekant {
    clip-path: polygon(0% 50%, 50% 100%, 100% 50%);
    -webkit-clip-path: polygon(0% 50%, 50% 100%, 100% 50%);
  }
</style>

<div id="eksempel" 
     class="steg-1" 
     onclick="((e,obj) => {if (obj.className==='steg-1') { obj.className = 'steg-2'} else { obj.className = 'steg-1'}})(event,this)">
  <div class="trekant">TRYKK PÅ MEG</div>
</div>

HTMLen til trekanten over er dette:

```html
<div id="eksempel" 
     class="steg-1"
     onclick="...">
  <div class="trekant">TRYKK PÅ MEG</div>
</div>
```

Stylingen er slik:

```html
<style>
  .trekant {
    /* Sett en høyde og bredde, samt farge */
    width: 100px;
    height: 100px; 
    background: pink;
 
    /* Denne sørger for at clip-pathen animerer */
    transition: all 2s ease;
  
    /* Småjusteringer */
    margin-left: 50px;
    font-size: 0.5rem; 
    text-align: center;
  }

  /* Tilstand til steg 1 */
  .steg-1 > .trekant {
    clip-path: polygon(0% 0%, 100% 0, 50% 100%);
    /* Webkit trenger litt ekstra hjelp */
    -webkit-clip-path: polygon(0 0, 100% 0, 50% 100%);
  }

  /* Tilstand til steg 2 */
  .steg-2 > .trekant {
    clip-path: polygon(0% 50%, 50% 100%, 100% 50%);
    -webkit-clip-path: polygon(0% 50%, 50% 100%, 100% 50%);
  }
</style>
```

For at transisjonen skal fungere så må du ha likt antall punkter i polygonene.

Click-handler er tatt rett ut fra 2000-tallet. Det eneste den gjør er å alternere mellom stegene ved å bytte ut klassen.

```javascript
((e,obj) => {
  if (obj.className==='steg-1') { 
    obj.className = 'steg-2'
  } else { 
    obj.className = 'steg-1'
  }})(event,this)
```

Verre er det ikke. Dette er alle byggeklossene som skal til for å lage animasjonen mellom treet og hjorten.

`clip-path` er støtta i [de fleste nettlesere](https://caniuse.com/#search=clip-path), men enn så lenge så 
trenger webkit et prefiks (`-webkit-clip-path`).

## Oppsummert
Med `clip-path` så kan du transformere en vilkårlig sammensatt figur fra en form til en annen. Det eneste 
du trenger å gjøre er å deklarere hvordan hver figur skal se ut, så fikser nettleseren resten for deg! 


## For de ekstra interesserte
For å generere CSS-formene over så jukset jeg litt med et lite program skrevet i ClojureScript. 
Du kan [lese kildekoden](https://github.com/Odinodin/clippety) om du vil. Formene i eksemplet
tegnet jeg for hånd. Det neste jeg skal gjøre er å lage et program som transformerer vilkårlige bilder
til en datastruktur med trekant-koordinater.