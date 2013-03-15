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
S_xName sprintf "touch.%d.x", i_instanceNuma1 vco2 ky * .5 * kenv, 60 + (log(1 - kx) * 3000), 0
S_yName sprintf "touch.%d.y", i_instanceNum

kx chnget S_xName
ky chnget S_yName

kenv linsegr 0, .001, 1, .1, 1, .25, 0
kamp = ky * .5 * kenv
kfreq = kx*440
kjet = ky*rnd(2000)			
iatt = 0.1
idetk = 0.1
kngain = 0.0005
kvibf = rnd(100)
kvamp = 0.0005
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
