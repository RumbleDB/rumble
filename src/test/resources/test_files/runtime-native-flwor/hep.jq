module namespace hep = "hep.jq";
import module namespace math = "math.jq";

declare function hep:histogram($values, $lo, $hi, $num-bins) {
  let $width := float(($hi - $lo) div $num-bins)
  let $half-width := float($width div 2)

  let $underflow := float(round(($lo - $half-width) div $width))
  let $overflow := float(round(($hi - $half-width) div $width))
  return
  for $v in $values
  let $bucket-idx :=
    if ($v lt $lo) then exactly-one($underflow)
    else
      if ($v gt $hi) then exactly-one($overflow)
      else round(($v - $half-width) div $width)
  let $center := $bucket-idx * $width + $half-width

  group by $center
  order by $center
  return {"x": $center, "y": count($v)}
};

declare function hep:make-muons($event) {
  for $i in (1 to size($event.Muon_pt))
  return {
    "pt": $event.Muon_pt[[$i]],
    "eta": $event.Muon_eta[[$i]],
    "phi": $event.Muon_phi[[$i]],
    "mass": $event.Muon_mass[[$i]],
    "charge": $event.Muon_charge[[$i]],
    "pfRelIso03_all": $event.Muon_pfRelIso03_all[[$i]],
    "pfRelIso04_all": $event.Muon_pfRelIso04_all[[$i]],
    "tightId": $event.Muon_tightId[[$i]],
    "softId": $event.Muon_softId[[$i]],
    "dxy": $event.Muon_dxy[[$i]],
    "dxyErr": $event.Muon_dxyErr[[$i]],
    "dz": $event.Muon_dz[[$i]],
    "dzErr": $event.Muon_dzErr[[$i]],
    "jetIdx": $event.Muon_jetIdx[[$i]],
    "genPartIdx": $event.Muon_genPartIdx[[$i]]
  }
};

declare function hep:make-electrons($event) {
  for $i in (1 to size($event.Electron_pt))
  return {
    "pt": $event.Electron_pt[[$i]],
    "eta": $event.Electron_eta[[$i]],
    "phi": $event.Electron_phi[[$i]],
    "mass": $event.Electron_mass[[$i]],
    "charge": $event.Electron_charge[[$i]],
    "pfRelIso03_all": $event.Electron_pfRelIso03_all[[$i]],
    "dxy": $event.Electron_dxy[[$i]],
    "dxyErr": $event.Electron_dxyErr[[$i]],
    "dz": $event.Electron_dz[[$i]],
    "dzErr": $event.Electron_dzErr[[$i]],
    "cutBasedId": $event.Electron_cutBasedId[[$i]],
    "pfId": $event.Electron_pfId[[$i]],
    "jetIdx": $event.Electron_jetIdx[[$i]],
    "genPartIdx": $event.Electron_genPartIdx[[$i]]
  }
};

declare function hep:make-jets($event) {
  for $i in (1 to size($event.Jet_pt))
  return {
    "pt": $event.Jet_pt[[$i]],
    "eta": $event.Jet_eta[[$i]],
    "phi": $event.Jet_phi[[$i]],
    "mass": $event.Jet_mass[[$i]],
    "puId": $event.Jet_puId[[$i]],
    "btag": $event.Jet_btag[[$i]]
  }
};

declare function hep:restructure-event($event) {
  let $muons := hep:make-muons($event)
  let $electrons := hep:make-electrons($event)
  let $jets := hep:make-jets($event)
  return {| $event,
           {
              "Muon": [ $muons ],
              "Electron": [ $electrons ],
              "Jet": [ $jets ]
           }
         |}
};

declare function hep:restructure-data($data) {
  for $event in $data
  return hep:restructure-event($event)
};

declare function hep:restructure-data-parquet($path) {
  for $event in parquet-file($path)
  return hep:restructure-event($event)
};

declare function hep:compute-invariant-mass($m1, $m2) {
  2 * $m1.pt * $m2.pt * (math:cosh($m1.eta - $m2.eta) - cos($m1.phi - $m2.phi))
};

declare function hep:PtEtaPhiM-to-PxPyPzE($vect) {
  let $x := $vect.pt * cos($vect.phi)
  let $y := $vect.pt * sin($vect.phi)
  let $z := $vect.pt * math:sinh($vect.eta)
  let $temp := $vect.pt * math:cosh($vect.eta)
  let $e := sqrt($temp * $temp + $vect.mass * $vect.mass)
  return {"x": $x, "y": $y, "z": $z, "e": $e}
};

declare function hep:add-PxPyPzE($particle1, $particle2) {
  let $x := $particle1.x + $particle2.x
  let $y := $particle1.y + $particle2.y
  let $z := $particle1.z + $particle2.z
  let $e := $particle1.e + $particle2.e
  return {"x": $x, "y": $y, "z": $z, "e": $e}
};

declare function hep:add-PxPyPzE($particle1, $particle2, $particle3) {
  let $x := $particle1.x + $particle2.x + $particle3.x
  let $y := $particle1.y + $particle2.y + $particle3.y
  let $z := $particle1.z + $particle2.z + $particle3.z
  let $e := $particle1.e + $particle2.e + $particle3.e
  return {"x": $x, "y": $y, "z": $z, "e": $e}
};

declare function hep:RhoZ-to-eta($rho, $z) {
  let $temp := $z div $rho
  return log($temp + sqrt($temp * $temp + 1.0))
};

declare function hep:PxPyPzE-to-PtEtaPhiM($particle) {
  let $x2 := $particle.x * $particle.x
  let $y2 := $particle.y * $particle.y
  let $z2 := $particle.z * $particle.z
  let $e2 := $particle.e * $particle.e

  let $pt := sqrt($x2 + $y2)
  let $eta := hep:RhoZ-to-eta($pt, $particle.z)
  let $phi := if ($particle.x = 0.0 and $particle.y = 0.0)
        then 0.0
        else atan2($particle.y, $particle.x)
  let $mass := sqrt($e2 - $z2 - $y2 - $x2)

  return {"pt": $pt, "eta": $eta, "phi": $phi, "mass": $mass}
};

declare function hep:make-tri-jet($particle1, $particle2, $particle3) {
  hep:PxPyPzE-to-PtEtaPhiM(
    hep:add-PxPyPzE(
      hep:PtEtaPhiM-to-PxPyPzE($particle1),
      hep:PtEtaPhiM-to-PxPyPzE($particle2),
      hep:PtEtaPhiM-to-PxPyPzE($particle3)
      )
    )
};

declare function hep:add-PtEtaPhiM($particle1, $particle2) {
  hep:PxPyPzE-to-PtEtaPhiM(
    hep:add-PxPyPzE(
      hep:PtEtaPhiM-to-PxPyPzE($particle1),
      hep:PtEtaPhiM-to-PxPyPzE($particle2)
      )
    )
};

declare function hep:delta-phi($phi1, $phi2) {
  ($phi1 - $phi2 + pi()) mod (2 * pi()) - pi()
};

declare function hep:delta-R($p1, $p2) {
  let $delta-eta := $p1.eta - $p2.eta
  let $delta-phi := hep:delta-phi($p1.phi, $p2.phi)
  return sqrt($delta-phi * $delta-phi + $delta-eta * $delta-eta)
};

declare function hep:concat-leptons($event) {
  let $muons := (
    for $muon in $event.Muon[]
    return {| $muon, {"type": "m"}  |}
  )

  let $electrons := (
    for $electron in $event.Electron[]
    return {| $electron, {"type": "e"}  |}
  )

  return ($muons, $electrons)
};
