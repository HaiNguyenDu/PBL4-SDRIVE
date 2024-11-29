@echo off
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /inheritance:r
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /remove "PBL4\Phuc"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /remove "PBL4\Phuc" /T
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /grant "PBL4\Phuc":(OI)(CI)R
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah\*" /grant "PBL4\Phuc":(OI)(CI)R /T
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /grant "PBL4\Administrator:(OI)(CI)F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah" /grant "PBL4\XPhuc:(OI)(CI)F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah\*" /grant "PBL4\Administrator:(OI)(CI)F" /T
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\hah\*" /grant "PBL4\XPhuc:(OI)(CI)F" /T
