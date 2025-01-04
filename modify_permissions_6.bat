@echo off
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /inheritance:r
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /remove "PBL4\XPhuc"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /remove "PBL4\XPhuc" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\XPhuc":(OI)(CI)R
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\XPhuc":(OI)(CI)R /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4" /grant "PBL4\Thanhan:(OI)(CI)F"
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\192.168.10.25\SDriver\Thanhan\Toilampbl4\*" /grant "PBL4\Thanhan:(OI)(CI)F" /T
