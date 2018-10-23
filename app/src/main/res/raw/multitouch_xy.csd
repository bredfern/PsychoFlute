<CsoundSynthesizer>
<CsOptions>
-o dac -d -b512 -B2048
</CsOptions>
<CsInstruments>
nchnls=2
0dbfs=1
ksmps=32
sr = 44100
ga1 init 0

instr 1
itie tival
i_instanceNum = p4
S_xName sprintf "touch.%d.x", i_instanceNum
S_yName sprintf "touch.%d.y", i_instanceNum

kx chnget S_xName
ky chnget S_yName

kaccelX chnget "accelerometerX" 
kaccelY chnget "accelerometerY"


kenv linsegr 0, .0001, 1, .1, 1, .25, 0
kamp = 0.1 * kenv
kfreq = octpch(kx*50)
kjet = ky + kaccelY*1000
iatt = 0.1
idetk = 0.1
kngain = 0.005
kvibf = ky + kaccelX*1000
kvamp = 0.00005
ifn = 1
asig wgflute kamp, kfreq, kjet, iatt, idetk, kngain, kvibf, kvamp, ifn
ga1 = ga1 + asig
endin

instr 2
kcutoff = 6000
kresonance = .2
a1 moogladder ga1, kcutoff, kresonance
denorm a1
aL, aR  freeverb a1, a1, 0.98, 0.35, sr, 0
outs a1 + aL, a1 + aR
ga1 = 0
endin


</CsInstruments>
<CsScore>
f1 0 16384 10 1

i2 0 360000
 
</CsScore>
</CsoundSynthesizer>