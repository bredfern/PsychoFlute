<CsoundSynthesizer>
<CsOptions>
-o dac -+rtaudio=null -d   -b512
</CsOptions>
<CsInstruments>
nchnls=2
0dbfs=1
ksmps=64
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

kenv linsegr 0, .001, 1, .1, 1, .25, 0

kamp = ky * .46 * kenv 
kfreq = kx*100 
kjet = rnd(900) + ky + kaccelY * 0.001 		
iatt = 0.1
idetk = 0.1
kngain = 0.005
kvibf = rnd(10000) + ky + kaccelX * 0.001
kvamp = 0.00005
ifn = 1

asig wgflute kamp, kfreq, kjet, iatt, idetk, kngain, kvibf, kvamp, ifn

ga1 = ga1 + asig

endin

instr 2

;kcutoff chnget "cutoff"
;kresonance chnget "resonance"

kcutoff = 6000
kresonance = .2


a1 moogladder ga1, kcutoff, kresonance

aL, aR reverbsc a1, a1, .72, 5000

outs aL, aR

ga1 = 0

endin


</CsInstruments>
<CsScore>
f1 0 16384 10 1

i2 0 360000
 
</CsScore>
</CsoundSynthesizer>